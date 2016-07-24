package readimg1;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 *
 * FixedLengthRecordReader is returned by FixedLengthInputFormat. This reader
 * uses the record length property set within the FixedLengthInputFormat to
 * read one record at a time from the given InputSplit. This record reader
 * does not support compressed files.


 *
 * Each call to nextKeyValue() updates the LongWritable KEY and Text VALUE.


 *
 * KEY = byte position in the file the record started at

 * VALUE = the record itself (Text)
 *
 *
 * @author bitsofinfo.g (AT) gmail.com
 *
 */
public class PixelRecordReader extends RecordReader<LongWritable, BytesWritable> {

	// reference to the logger
	private static final Log LOG = LogFactory.getLog(PixelRecordReader.class);

	// the start point of our split
	private long splitStart;

	// the end point in our split
	private long splitEnd;

	// our current position in the split
	private long currentPosition;

	// the length of a record
	private int recordLength;

	// reference to the input stream
	private FSDataInputStream fileInputStream;

	 private int i=0;
	// the input byte counter
//	private Counter inputByteCounter;

	// reference to our FileSplit
	private FileSplit fileSplit;

	// our record key (byte position)
	private LongWritable recordKey = null;

	// the record value
	private BytesWritable recordValue = null;

	@Override
	public void close() throws IOException {
		if (fileInputStream != null) {
			fileInputStream.close();
		}
	}

	@Override
	public LongWritable getCurrentKey() throws IOException,
			InterruptedException {
		return recordKey;
	}

	@Override
	public BytesWritable getCurrentValue() throws IOException, InterruptedException {
		return recordValue;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		if (splitStart == splitEnd) {
			return (float)0;
		} else {
			return Math.min((float)1.0, (currentPosition - splitStart) / (float)(splitEnd - splitStart));
		}
	}

	@Override
	public void initialize(InputSplit inputSplit, TaskAttemptContext context)
			throws IOException, InterruptedException {

		// the file input fileSplit
		this.fileSplit = (FileSplit)inputSplit;

		// the byte position this fileSplit starts at within the splitEnd file
		splitStart = fileSplit.getStart()+19612;

		// splitEnd byte marker that the fileSplit ends at within the splitEnd file
		splitEnd = splitStart +19612*2600;//fileSplit.getLength();
		
		// log some debug info
		LOG.info("FixedLengthRecordReader: SPLIT START="+splitStart + " SPLIT END=" +splitEnd + " SPLIT LENGTH="+fileSplit.getLength() );

		// the actual file we will be reading from
		Path file = fileSplit.getPath();

		// job configuration
		Configuration job = context.getConfiguration();


		// for updating the total bytes read in
	 	//inputByteCounter = ((MapContext)context).getCounter("FileInputFormatCounters", "BYTES_READ");

	 	// THE JAR COMPILED AGAINST 0.20.1 does not contain a version of FileInputFormat with these constants (but they exist in trunk)
	 	// uncomment the below, then comment or discard the line above
	 	//inputByteCounter = ((MapContext)context).getCounter(FileInputFormat.COUNTER_GROUP, FileInputFormat.BYTES_READ);

		// the size of each fixed length record
		this.recordLength = 19612*2600;

		// get the filesystem
		final FileSystem fs = file.getFileSystem(job);

		// open the File
   //splitStart=19600;
		fileInputStream = fs.open(file);

		// seek to the splitStart position
		fileInputStream.seek(splitStart);

		// set our current position
	 	this.currentPosition = splitStart;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (recordKey == null) {
		 	recordKey = new LongWritable();
	 	}

		// the Key is always the position the record starts at
	 	recordKey.set(currentPosition);

	 	// the recordValue to place the record text in
	 	if (recordValue == null) {
	 		recordValue = new BytesWritable();
	 	} else {
	 	
	 	}
   
		 	// if the currentPosition is less than the split end..
	 	if (currentPosition < splitEnd) {

	 		// setup a buffer to store the record
	 		byte[] buffer = new byte[19612*2600];
	 		long  totalRead = 0; // total bytes read
	 		int rd=0;
	 		// while we still have record bytes to read
	 		while(totalRead != recordLength && rd!=-1) {
	 			
	 			// read in what we need
	 				
	 			 rd = this.fileInputStream.read(buffer,rd,19612*2600-rd);// totalToRead);

	 			LOG.info("FixedLengthRecordReader:"+i+ "SPLIT CURRENT="+currentPosition+"SPLIT START="+splitStart + " SPLIT END=" +splitEnd + " SPLIT LENGTH="+fileSplit.getLength()+"AVAILABLE=="+fileInputStream.available() +"Total Read="+totalRead+" Rd="+rd );

	 		
	 			// append to the buffer
	 			
	 		
	 			// update our markers
	 			totalRead += rd;
	 			i++;
	 		}
	 		
	 		if(rd==-1)
	 		{
	 			LOG.info("In rd -1 FixedLengthRecordReader:"+i+ "SPLIT CURRENT="+currentPosition+"SPLIT START="+splitStart + " SPLIT END=" +splitEnd + " SPLIT LENGTH="+fileSplit.getLength()+"AVAILABLE=="+fileInputStream.available() +"Total Read="+totalRead+" Rd="+rd );
		
	 			recordValue.set(buffer,0,buffer.length);
	 			System.out.println(buffer.length);
	 		}
	 		else
	 		{
	 			recordValue.set(buffer,0,recordLength);
	 			System.out.println(buffer.length);
	 		}
	 		currentPosition = currentPosition +recordLength;
	 		// update our current position and log the input bytes
	 		//currentPosition = currentPosition +recordLength;
	 		//inputByteCounter.increment(recordLength);


	 		// return true
	 		return true;
	 	}

	 	// nothing more to read....
		return false;
	}

}


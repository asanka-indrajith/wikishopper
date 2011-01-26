package com.ai.android.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
/*
 * As mentioned at the official Android developers blog post, there is a bug in the previous versions of the decodeStream method that may cause problems when downloading an image over a slow connection
 * This class used to fix that bug.
 * which extends FilterInputStream, is used instead in order to fix the problem.
 * */
public class FlushedInputStream extends FilterInputStream {
	
    public FlushedInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public long skip(long n) throws IOException {                              //This ensures that skip() method actually skips the provided number of bytes, unless we reach the end of file
        long totalBytesSkipped = 0L;
        while (totalBytesSkipped < n) {
            long bytesSkipped = in.skip(n - totalBytesSkipped);
            if (bytesSkipped == 0L) {
                  int b = read();
                  if (b < 0) {
                      break;                           //  reached EOF
                  } else {
                      bytesSkipped = 1;                //  read one byte
                  }
           }
            totalBytesSkipped += bytesSkipped;
        }
        return totalBytesSkipped;
    }
    
}
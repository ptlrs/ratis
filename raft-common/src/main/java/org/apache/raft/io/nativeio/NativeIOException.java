/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.raft.io.nativeio;

import org.apache.hadoop.util.Shell;

import java.io.IOException;


/**
 * An exception generated by a call to the native IO code.
 *
 * These exceptions simply wrap <i>errno</i> result codes on Linux,
 * or the System Error Code on Windows.
 */
public class NativeIOException extends IOException {
  private static final long serialVersionUID = 1L;

  private Errno errno;

  // Java has no unsigned primitive error code. Use a signed 32-bit
  // integer to hold the unsigned 32-bit integer.
  private int errorCode;

  public NativeIOException(String msg, Errno errno) {
    super(msg);
    this.errno = errno;
    // Windows error code is always set to ERROR_SUCCESS on Linux,
    // i.e. no failure on Windows
    this.errorCode = 0;
  }

  public NativeIOException(String msg, int errorCode) {
    super(msg);
    this.errorCode = errorCode;
    this.errno = Errno.UNKNOWN;
  }

  public long getErrorCode() {
    return errorCode;
  }

  public Errno getErrno() {
    return errno;
  }

  @Override
  public String toString() {
    if (Shell.WINDOWS)
      return errorCode + ": " + super.getMessage();
    else
      return errno.toString() + ": " + super.getMessage();
  }
}


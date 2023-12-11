/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.goms.module.common.util;

import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class ExifUtil {
    private static final String TAG = "CameraExif";

    // Returns the degrees in clockwise. Values are 0, 90, 180, or 270.
    public static int getOrientation(byte[] jpeg) {
        if (jpeg == null) {
            return 0;
        }

        int offset = 0;
        int length = 0;

        // ISO/IEC 10918-1:1993(E)
        while (offset + 3 < jpeg.length && (jpeg[offset++] & 0xFF) == 0xFF) {
            int marker = jpeg[offset] & 0xFF;

            // Check if the marker is a padding.
            if (marker == 0xFF) {
                continue;
            }
            offset++;

            // Check if the marker is SOI or TEM.
            if (marker == 0xD8 || marker == 0x01) {
                continue;
            }
            // Check if the marker is EOI or SOS.
            if (marker == 0xD9 || marker == 0xDA) {
                break;
            }

            // Get the length and check if it is reasonable.
            length = pack(jpeg, offset, 2, false);
            if (length < 2 || offset + length > jpeg.length) {
                Log.e(TAG, "Invalid length");
                return 0;
            }

            // Break if the marker is EXIF in APP1.
            if (marker == 0xE1 && length >= 8 &&
                    pack(jpeg, offset + 2, 4, false) == 0x45786966 &&
                    pack(jpeg, offset + 6, 2, false) == 0) {
                offset += 8;
                length -= 8;
                break;
            }

            // Skip other markers.
            offset += length;
            length = 0;
        }

        // JEITA CP-3451 Exif Version 2.2
        if (length > 8) {
            // Identify the byte order.
            int tag = pack(jpeg, offset, 4, false);
            if (tag != 0x49492A00 && tag != 0x4D4D002A) {
                Log.e(TAG, "Invalid byte order");
                return 0;
            }
            boolean littleEndian = (tag == 0x49492A00);

            // Get the offset and check if it is reasonable.
            int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
            if (count < 10 || count > length) {
                Log.e(TAG, "Invalid offset");
                return 0;
            }
            offset += count;
            length -= count;

            // Get the count and go through all the elements.
            count = pack(jpeg, offset - 2, 2, littleEndian);
            while (count-- > 0 && length >= 12) {
                // Get the tag and check if it is orientation.
                tag = pack(jpeg, offset, 2, littleEndian);
                if (tag == 0x0112) {
                    // We do not really care about type and count, do we?
                    int orientation = pack(jpeg, offset + 8, 2, littleEndian);
                    switch (orientation) {
                        case 1:
                            return 0;
                        case 3:
                            return 180;
                        case 6:
                            return 90;
                        case 8:
                            return 270;
                    }
                    Log.i(TAG, "Unsupported orientation");
                    return 0;
                }
                offset += 12;
                length -= 12;
            }
        }

        Log.i(TAG, "Orientation not found");
        return 0;
    }

    private static int pack(byte[] bytes, int offset, int length,
            boolean littleEndian) {
        int step = 1;
        if (littleEndian) {
            offset += length - 1;
            step = -1;
        }

        int value = 0;
        while (length-- > 0) {
            value = (value << 8) | (bytes[offset] & 0xFF);
            offset += step;
        }
        return value;
    }

    public static void setPhotoExif(String originalPath, String targetPath){
        File mTempFile = new File(targetPath);

        /** Orientation 처리 */
        int applyOrientation = 90;
        int exif_orientation_s = ExifInterface.ORIENTATION_UNDEFINED;

        try {
            ExifInterface exif = new ExifInterface(originalPath);
            String exif_aperture = exif.getAttribute(ExifInterface.TAG_APERTURE);
            String exif_datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
            String exif_exposure_time = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            String exif_flash = exif.getAttribute(ExifInterface.TAG_FLASH);
            String exif_focal_length = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            String exif_iso = exif.getAttribute(ExifInterface.TAG_ISO);
            String exif_make = exif.getAttribute(ExifInterface.TAG_MAKE);
            String exif_model = exif.getAttribute(ExifInterface.TAG_MODEL);
            int exif_orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            exif_orientation_s = exif_orientation; // store for later use (for the thumbnail, to save rereading it)
            String exif_white_balance = exif.getAttribute(ExifInterface.TAG_WHITE_BALANCE);

            GomsLog.d(TAG, "exif_flash  : " + exif_flash);
            GomsLog.d(TAG, "exif_make : " + exif_make);
            GomsLog.d(TAG, "exif_iso : " + exif_iso);
            GomsLog.d(TAG, "exif_model : " + exif_model);
            GomsLog.d(TAG, "exif_orientation : " + exif_orientation);
            GomsLog.d(TAG, "exif_orientation_s : " + exif_orientation_s);
            GomsLog.d(TAG, "exif_white_balance : " + exif_white_balance);
            GomsLog.d(TAG, "exif_exposure_time : " + exif_exposure_time);
            GomsLog.d(TAG, "exif_datetime : " + exif_datetime);

            ExifInterface exif_new = new ExifInterface(mTempFile.getAbsolutePath());
            if( exif_aperture != null )
                exif_new.setAttribute(ExifInterface.TAG_APERTURE, exif_aperture);
            if( exif_datetime != null )
                exif_new.setAttribute(ExifInterface.TAG_DATETIME, exif_datetime);
            if( exif_exposure_time != null )
                exif_new.setAttribute(ExifInterface.TAG_EXPOSURE_TIME, exif_exposure_time);
            if( exif_flash != null )
                exif_new.setAttribute(ExifInterface.TAG_FLASH, exif_flash);
            if( exif_focal_length != null )
                exif_new.setAttribute(ExifInterface.TAG_FOCAL_LENGTH, exif_focal_length);
            ;
            // leave width/height, as this will have changed!
            if( exif_iso != null )
                exif_new.setAttribute(ExifInterface.TAG_ISO, exif_iso);
            if( exif_make != null )
                exif_new.setAttribute(ExifInterface.TAG_MAKE, exif_make);
            if( exif_model != null )
                exif_new.setAttribute(ExifInterface.TAG_MODEL, exif_model);
            if( exif_orientation != ExifInterface.ORIENTATION_UNDEFINED )
                exif_new.setAttribute(ExifInterface.TAG_ORIENTATION, "" + exif_orientation);
            if( exif_white_balance != null )
                exif_new.setAttribute(ExifInterface.TAG_WHITE_BALANCE, exif_white_balance);
            setDateTimeExif(exif_new);

            exif_new.saveAttributes();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static void setDateTimeExif(ExifInterface exif) {
        String exif_datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
        if( exif_datetime != null ) {
            exif.setAttribute("DateTimeOriginal", exif_datetime);
            exif.setAttribute("DateTimeDigitized", exif_datetime);
        }
    }
}

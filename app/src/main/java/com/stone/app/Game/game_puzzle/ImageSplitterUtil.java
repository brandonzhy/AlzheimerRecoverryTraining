package com.stone.app.Game.game_puzzle;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class ImageSplitterUtil {
    public static List<image> split(Bitmap bitmap, int piece) {

        List<image> pieces = new ArrayList<image>(piece*piece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.i("TAG","width= " +width );
        Log.i("TAG","height= " +height );

        int pieceWidth=Math.min(width,height)/piece;
        for (int i = 0; i < piece; i++) {
            for (int j = 0; j < piece; j++) {
                image Apiece = new image();
               Apiece.index=j + i * piece;
                int x = j * pieceWidth;
                int y= i * pieceWidth;
                Apiece.bitmap=Bitmap.createBitmap( bitmap, x, y,
                        pieceWidth,pieceWidth);

                pieces.add(Apiece);
            }
        }

        return pieces;
    }

}

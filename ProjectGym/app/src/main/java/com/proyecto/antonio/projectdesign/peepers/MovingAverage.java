package com.proyecto.antonio.projectdesign.peepers;

/**
 * Created by usuario on 15/04/2015.
 */
public class MovingAverage {

    private final int mNumValues;
    private final long[] mValues;
    private int mEnd = 0;
    private int mLength = 0;
    private long mSum = 0L;

    MovingAverage(final int numValues){
        super();
        mNumValues = numValues;
        mValues = new long[numValues];
    }

    void update(final long value){
        mSum -= mValues[mEnd];
        mValues[mEnd] = value;
        mEnd = (mEnd + 1) % mNumValues;
        if(mLength < mNumValues){
            mLength++;
        }
        mSum += value;
    }

    double getAverage(){
        return mSum / (double) mLength;
    }
}

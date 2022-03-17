/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.vlath.keyboardview.latin.inputlogic;

import android.os.HandlerThread;

import com.vlath.keyboardview.latin.LatinIME;
import com.vlath.keyboardview.latin.common.InputPointers;

/**
 * A helper to manage deferred tasks for the input logic.
 */
class InputLogicHandler {
    // TODO: remove this reference.
    final LatinIME mLatinIME;
    final InputLogic mInputLogic;
    private final Object mLock = new Object();
    private boolean mInBatchInput; // synchronized using {@link #mLock}.

    // A handler that never does anything. This is used for cases where events come before anything
    // is initialized, though probably only the monkey can actually do this.
    public static final InputLogicHandler NULL_HANDLER = new InputLogicHandler() {
        @Override
        public void onStartBatchInput() {}
        @Override
        public void onUpdateBatchInput(final InputPointers batchPointers,
                final int sequenceNumber) {}
        @Override
        public void onCancelBatchInput() {}
        @Override
        public void updateTailBatchInput(final InputPointers batchPointers,
                final int sequenceNumber) {}
    };

    InputLogicHandler() {
        mLatinIME = null;
        mInputLogic = null;
    }

    public InputLogicHandler(final LatinIME latinIME, final InputLogic inputLogic) {
        final HandlerThread handlerThread = new HandlerThread(
                InputLogicHandler.class.getSimpleName());
        handlerThread.start();
        mLatinIME = latinIME;
        mInputLogic = inputLogic;
    }

    // Called on the UI thread by InputLogic.
    public void onStartBatchInput() {
        synchronized (mLock) {
            mInBatchInput = true;
        }
    }

    public boolean isInBatchInput() {
        return mInBatchInput;
    }

    /**
     * Fetch suggestions corresponding to an update of a batch input.
     * @param batchPointers the updated pointers, including the part that was passed last time.
     * @param sequenceNumber the sequence number associated with this batch input.
     * @param isTailBatchInput true if this is the end of a batch input, false if it's an update.
     */
    // This method can be called from any thread and will see to it that the correct threads
    // are used for parts that require it. This method will send a message to the Non-UI handler
    // thread to pull suggestions, and get the inlined callback to get called on the Non-UI
    // handler thread. If this is the end of a batch input, the callback will then proceed to
    // send a message to the UI handler in LatinIME so that showing suggestions can be done on
    // the UI thread.
    private void updateBatchInput(final InputPointers batchPointers,
            final int sequenceNumber, final boolean isTailBatchInput) {
        synchronized (mLock) {
            if (!mInBatchInput) {
                // Batch input has ended or canceled while the message was being delivered.
                return;
            }
            mInputLogic.mWordComposer.setBatchInputPointers(batchPointers);
        }
    }

    /**
     * Update a batch input.
     *
     * This fetches suggestions and updates the suggestion strip and the floating text preview.
     *
     * @param batchPointers the updated batch pointers.
     * @param sequenceNumber the sequence number associated with this batch input.
     */
    // Called on the UI thread by InputLogic.
    public void onUpdateBatchInput(final InputPointers batchPointers,
            final int sequenceNumber) {
        updateBatchInput(batchPointers, sequenceNumber, false /* isTailBatchInput */);
    }

    /**
     * Cancel a batch input.
     *
     * Note that as opposed to updateTailBatchInput, we do the UI side of this immediately on the
     * same thread, rather than get this to call a method in LatinIME. This is because
     * canceling a batch input does not necessitate the long operation of pulling suggestions.
     */
    // Called on the UI thread by InputLogic.
    public void onCancelBatchInput() {
        synchronized (mLock) {
            mInBatchInput = false;
        }
    }

    /**
     * Trigger an update for a tail batch input.
     *
     * A tail batch input is the last update for a gesture, the one that is triggered after the
     * user lifts their finger. This method schedules fetching suggestions on the non-UI thread,
     * then when the suggestions are computed it comes back on the UI thread to update the
     * suggestion strip, commit the first suggestion, and dismiss the floating text preview.
     *
     * @param batchPointers the updated batch pointers.
     * @param sequenceNumber the sequence number associated with this batch input.
     */
    // Called on the UI thread by InputLogic.
    public void updateTailBatchInput(final InputPointers batchPointers,
            final int sequenceNumber) {
        updateBatchInput(batchPointers, sequenceNumber, true /* isTailBatchInput */);
    }
}

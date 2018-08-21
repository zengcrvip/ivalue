package com.axon.market.common.monitor;

/**
 * Created by chenyu on 2016/5/31.
 */
public class FileAlterationListenerAdaptor extends FileAlterationListener
{

    @Override
    public boolean accept(FileEntry fileEntry)
    {
        return false;
    }

    @Override
    public void directoryCreateAction(FileEntry fileEntry)
    {
    }

    @Override
    public void directoryChangeAction(FileEntry fileEntry)
    {
    }

    @Override
    public void directoryDeleteAction(FileEntry fileEntry)
    {
    }

    @Override
    public void fileCreateAction(FileEntry fileEntry)
    {
    }

    @Override
    public void fileChangeAction(FileEntry fileEntry)
    {
    }

    @Override
    public void fileDeleteAction(FileEntry fileEntry)
    {
    }

}

package com.example.braguia.viewmodel;

import androidx.arch.core.executor.TaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.braguia.model.app.AppInfo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class AppInfoViewModelTest {

    @Mock
    AppInfoViewModel viewModel;

    @Mock
    LiveData<AppInfo> appInfoLiveData;

    @Mock
    Observer<AppInfo> observer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAppInfo_returnsLiveData() {
        when(viewModel.getAppInfo()).thenReturn(appInfoLiveData);

        LiveData<AppInfo> result = viewModel.getAppInfo();

        verify(viewModel).getAppInfo();
        assert(result).equals(appInfoLiveData);
    }

    @Test
    public void getAppInfo_observesLiveData() throws InterruptedException, IOException {
        AppInfo appInfo = new AppInfo();
        appInfo.setVersionCode(1);
        appInfo.setVersionName("1.0.0");
        when(appInfoLiveData.getValue()).thenReturn(appInfo);

        viewModel.appInfo = appInfoLiveData;
        viewModel.getAppInfo().observeForever(observer);

        final CountDownLatch latch = new CountDownLatch(1);
        latch.await(2, TimeUnit.SECONDS);

        verify(observer).onChanged(appInfo);
    }
}

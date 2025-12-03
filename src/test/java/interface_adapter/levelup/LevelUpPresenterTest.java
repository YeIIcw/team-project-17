package interface_adapter.levelup;

import interface_adapter.LevelUp.LevelUpPresenter;
import interface_adapter.LevelUp.LevelUpViewCallback;
import interface_adapter.LevelUp.LevelUpViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.levelup.LevelUpOutputData;

import static org.junit.jupiter.api.Assertions.*;

class LevelUpPresenterTest {

    private LevelUpViewModel viewModel;
    private LevelUpPresenter presenter;

    @BeforeEach
    void setUp() {
        viewModel = new LevelUpViewModel();
        presenter = new LevelUpPresenter(viewModel);
    }

    // ------------------ Manual test doubles ------------------

    static class TestCallback implements LevelUpViewCallback {
        boolean showCalled = false;
        boolean hideCalled = false;
        LevelUpViewModel lastViewModel = null;

        @Override
        public void showLevelUpScreen(LevelUpViewModel vm) {
            showCalled = true;
            lastViewModel = vm;
        }

        @Override
        public void hideLevelUpScreen() {
            hideCalled = true;
        }
    }

    static class TestRunnable implements Runnable {
        boolean runCalled = false;

        @Override
        public void run() {
            runCalled = true;
        }
    }

    // ------------------ Tests ------------------

    @Test
    void testPresentUpdatedStatsUpdatesViewModel() {
        LevelUpOutputData data = new LevelUpOutputData(50, 10);

        presenter.presentUpdatedStats(data);

        assertEquals(50, viewModel.getHealth());
        assertEquals(10, viewModel.getDamage());
    }

    @Test
    void testPresentUpdatedStatsCallsCallback() {
        LevelUpOutputData data = new LevelUpOutputData(60, 12);
        TestCallback callback = new TestCallback();
        presenter.setViewCallback(callback);

        presenter.presentUpdatedStats(data);

        assertTrue(callback.showCalled);
        assertEquals(viewModel, callback.lastViewModel);
    }

    @Test
    void testLevelUpCompleteUpdatesViewModel() {
        LevelUpOutputData data = new LevelUpOutputData(70, 20);

        presenter.levelUpComplete(data);

        assertEquals(70, viewModel.getHealth());
        assertEquals(20, viewModel.getDamage());
    }

    @Test
    void testLevelUpCompleteCallsHide() {
        LevelUpOutputData data = new LevelUpOutputData(70, 20);
        TestCallback callback = new TestCallback();
        presenter.setViewCallback(callback);

        presenter.levelUpComplete(data);

        assertTrue(callback.hideCalled);
    }

    @Test
    void testLevelUpCompleteRunsFinishCallback() {
        LevelUpOutputData data = new LevelUpOutputData(70, 20);
        TestRunnable finishCallback = new TestRunnable();
        presenter.onLevelUpFinished = finishCallback;

        presenter.levelUpComplete(data);

        assertTrue(finishCallback.runCalled);
    }

    @Test
    void testNoCallbackDoesNotThrow() {
        LevelUpOutputData data = new LevelUpOutputData(30, 5);

        // No callback or finish set
        assertDoesNotThrow(() -> {
            presenter.presentUpdatedStats(data);
            presenter.levelUpComplete(data);
        });
    }
}

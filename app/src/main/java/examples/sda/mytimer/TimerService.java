package examples.sda.mytimer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by angelika on 13.06.17.
 */

public class TimerService extends Service {

		private IBinder binder = new LocalBinder();
		private Timer timer = new Timer();

		private int seconds;
		private boolean paused;

		public void startTimer(final int seconds, final TimerServiceListener listener) {

				this.seconds = seconds;
				paused = false;

				if (timer != null) {
						timer.cancel();
				}

				startCounting(listener);
		}

		public void pauseTimer(final TimerServiceListener listener) {
				if (paused) {
						startCounting(listener);
						paused = false;
						return;
				}

				timer.cancel();
				paused = true;
		}

		public void stopTimer(final int seconds, final TimerServiceListener listener) {
				timer.cancel();
				listener.onCounterUpdate(seconds);
		}

		private void startCounting(final TimerServiceListener listener) {
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
								if (--TimerService.this.seconds < 0) {
										timer.cancel();
										return;
								}

								listener.onCounterUpdate(TimerService.this.seconds);
						}
				}, 0, 1000);
		}

		@Override
		public IBinder onBind(Intent intent) {
				return binder;
		}

		//observer pattern
		public interface TimerServiceListener {
				void onCounterUpdate(int seconds);
		}

		public class LocalBinder extends Binder {
				public TimerService getService() {
						return TimerService.this;
				}
		}
}

package examples.sda.mytimer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements TimerService.TimerServiceListener {

		private static final int DEFAULT_TIMER = 10;

		private TimerService timerService;

		@BindView(R.id.textView_time)
		protected TextView textTime;

		@BindView(R.id.timer_text)
		protected EditText textTimer;

		@BindView(R.id.button_play)
		protected ImageButton buttonPlay;

		@BindView(R.id.button_pause)
		protected ImageButton buttonPause;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
				ButterKnife.bind(this);

				textTimer.setText(String.valueOf(DEFAULT_TIMER));
				textTimer.getText().toString();
				textTime.setText(String.valueOf(DEFAULT_TIMER));

				bindTimerService();

		}

		@OnClick({R.id.button_play, R.id.button_pause, R.id.button_stop})
		public void setButtonClick(View v) {

				int time = Integer.parseInt(textTimer.getText().toString());

				switch (v.getId()) {
						case R.id.button_play:
								timerService.startTimer(time, MainActivity.this);
								break;
						case R.id.button_pause:
								timerService.pauseTimer(MainActivity.this);
								break;
						case R.id.button_stop:
								timerService.stopTimer(time, MainActivity.this);
								break;
				}
		}

		private ServiceConnection serviceConnection = new ServiceConnection() {
				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
						timerService = ((TimerService.LocalBinder) service).getService();
				}

				@Override
				public void onServiceDisconnected(ComponentName name) {
						timerService = null;
				}
		};

		@BindView(R.id.button_stop)
		protected ImageButton buttonStop;

		private void bindTimerService() {
				Intent intent = new Intent(this, TimerService.class);
				bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
		}

		@Override
		protected void onDestroy() {
				super.onDestroy();
				unbindService(serviceConnection);
		}


		@Override
		public void onCounterUpdate(final int seconds) {
				runOnUiThread(new Runnable() {
						@Override
						public void run() {
								textTime.setText(String.valueOf(seconds));
						}
				});
		}
}

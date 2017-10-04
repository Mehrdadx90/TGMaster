package black.jack.tgmaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressWarnings("unused")
public class MainActivity extends Activity {
	public boolean isfirst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText text = (EditText) findViewById(R.id.editText2);
		final Button btn = (Button) findViewById(R.id.button1);
		final ImageView wb = (ImageView) findViewById(R.id.imageView1);
		final Spinner modes = (Spinner) findViewById(R.id.spinner1);
		final Spinner inner = (Spinner) findViewById(R.id.Spinner01);
		final Spinner glow = (Spinner) findViewById(R.id.spinner2);
		final Spinner back = (Spinner) findViewById(R.id.Spinner02);
		final Spinner font = (Spinner) findViewById(R.id.Spinner03);
		final Button share = (Button) findViewById(R.id.button2);
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intentShareFile = new Intent(Intent.ACTION_SEND);
				File fileWithinMyDir = new File(Environment.getExternalStorageDirectory(), "/res.gif");
				File fileWithinMyDir2 = new File(Environment.getExternalStorageDirectory(), "/res.png");

				if (fileWithinMyDir.exists()) {
					intentShareFile.setType("application/*");
					fileWithinMyDir.renameTo(new File(Environment.getExternalStorageDirectory(), "/res.gif"));
					intentShareFile.putExtra(Intent.EXTRA_STREAM,
							Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/res.gif"));

					intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "@BlackJack01");
					intentShareFile.putExtra(Intent.EXTRA_TEXT, "لوگوی زیر با برنامه TGMaster ساخته شده!");

					startActivity(Intent.createChooser(intentShareFile, "Share File"));
				}else if (fileWithinMyDir2.exists()) {
					intentShareFile.setType("application/*");
					fileWithinMyDir.renameTo(new File(Environment.getExternalStorageDirectory(), "/res.png"));
					intentShareFile.putExtra(Intent.EXTRA_STREAM,
							Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/res.png"));

					intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "@BlackJack01");
					intentShareFile.putExtra(Intent.EXTRA_TEXT, "لوگوی زیر با برنامه TGMaster ساخته شده!");

					startActivity(Intent.createChooser(intentShareFile, "Share File"));
				}
			}
		});
		String mode[] = { "Memories Anim Logo", "Alien Glow Anim Logo", "Whirl Anim Logo", "Blue Fire",
				"Shake Anim Logo",
				// Images
				"Matrix Logo", "Star Wars Logo", "Comics Logo", "3D Text Logo", "Water Logo", "BlackBird Logo",
				"Chrominium Logo", "Harry Potter Logo", "Inferno Logo", "Neon Logo", "Birdy Logo", "Flame Logo", "USA",
				"Scribble Logo", "Dracula Logo" };

		String color[] = { "سیاه", "سفید", "آبی", "قرمز", "فیروزه ای", "خاکستری", "سبز", "زرد" };
		String fonts[] = { "BlackChancery", "ziperhead", "Ethnocentric", "Barbatrick", "Loki Cola", "Alien League",
				"Becker", "Aladdin", "Bullpen", "Almonte Snow" };

		//

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				mode);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The
		modes.setAdapter(spinnerArrayAdapter);

		ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				fonts);
		spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The
		font.setAdapter(spinnerArrayAdapter1);

		// colors------------------------------------------------------------------------

		ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				color);
		spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The
		inner.setAdapter(spinnerArrayAdapter2);
		glow.setAdapter(spinnerArrayAdapter2);
		back.setAdapter(spinnerArrayAdapter2);

		// colors-------------------------------------------------------------------------

		final String[] prob = { ":", "(", ")", "/", ".", "?", "#", "@" };
		btn.setOnClickListener(new OnClickListener() {
			public boolean incorrect = false;
			public boolean istoasted = false;

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {

				String user = text.getText().toString();

				incorrect = false;
				if (!user.isEmpty()) {
					for (int i = 0; i < user.length(); i++) {
						String temp = user.substring(i, i + 1);
						if (temp.equals("/") || temp.equals("#") || temp.equals("@") || temp.equals('\n')) {
							incorrect = true;
							break;
						}
					}

				} else {
					incorrect = true;
				}
				if (incorrect) {
					Toast.makeText(getApplicationContext(), "ورودی غلط", Toast.LENGTH_LONG).show();
				} else {
					File ew = new File(Environment.getExternalStorageDirectory(), "/res");
					File ew2 = new File(Environment.getExternalStorageDirectory(), "/res.gif");
					File ew3 = new File(Environment.getExternalStorageDirectory(), "/res.png");
					if (ew.exists()) {
						ew.delete();
					} else if (ew2.exists()) {
						ew2.delete();
					} else if (ew3.exists()) {
						ew3.delete();
					}
					final ProgressDialog pd = new ProgressDialog(MainActivity.this);
					pd.setCancelable(false);
					pd.setMessage("در حال ساخت و ارسال لوگو");
					pd.show();
					new CountDownTimer(500, 500) {

						@Override
						public void onTick(long arg0) {

						}

						@Override
						public void onFinish() {
							pgs();
							pd.dismiss();
						}
					}.start();

					try {
					} catch (Exception e) {
					}

				}
				if (!btn.isEnabled()) {
				} else {
				}

			}

		});

	}

	private String getcolor(String Clr) {
		String color[] = { "سیاه", "سفید", "آبی", "قرمز", "فیروزه ای", "خاکستری", "سبز", "زرد" };
		switch (Clr) {
		case "سیاه":
			return Integer.toHexString(Color.BLACK).substring(2);

		case "سفید":
			return Integer.toHexString(Color.WHITE).substring(2);

		case "آبی":
			return Integer.toHexString(Color.BLUE).substring(2);

		case "قرمز":
			return Integer.toHexString(Color.RED).substring(2);

		case "فیروزه ای":
			return Integer.toHexString(Color.CYAN).substring(2);

		case "خاکستری":
			return Integer.toHexString(Color.GRAY).substring(2);

		case "سبز":
			return Integer.toHexString(Color.GREEN).substring(2);

		case "زرد":
			return Integer.toHexString(Color.YELLOW).substring(2);

		default:
			break;
		}

		return null;
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;

		public DownloadTask(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(String... sUrl) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP " + connection.getResponseCode() + " "
							+ connection.getResponseMessage();
				}
				int fileLength = connection.getContentLength();
				input = connection.getInputStream();
				output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/page");
				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (Exception e) {
				return e.toString();
			} catch (Throwable e) {
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

	}

	private class DownloadTask2 extends AsyncTask<String, Integer, String> {

		private Context context;

		public DownloadTask2(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(String... sUrl) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP " + connection.getResponseCode() + " "
							+ connection.getResponseMessage();
				}
				int fileLength = connection.getContentLength();
				input = connection.getInputStream();
				if (url.toString().contains(".gif")) {
					output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/res.gif");
				} else {
					output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/res.png");
				}

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (Exception e) {
				return e.toString();
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

	}

	public void pgs() {
		File ew2 = new File(Environment.getExternalStorageDirectory(), "/res.gif");
		File ew3 = new File(Environment.getExternalStorageDirectory(), "/res.png");
		final File res = new File(Environment.getExternalStorageDirectory(), "/res");
		final EditText text = (EditText) findViewById(R.id.editText2);
		final Button btn = (Button) findViewById(R.id.button1);
		final ImageView wb = (ImageView) findViewById(R.id.imageView1);
		final Spinner modes = (Spinner) findViewById(R.id.spinner1);
		final Spinner inner = (Spinner) findViewById(R.id.Spinner01);
		final Spinner glow = (Spinner) findViewById(R.id.spinner2);
		final Spinner back = (Spinner) findViewById(R.id.Spinner02);
		final Spinner font = (Spinner) findViewById(R.id.Spinner03);

		text.setText(text.getText().toString().replaceAll(" ", "%20"));

		final File pagetxt = new File(Environment.getExternalStorageDirectory(), "/page");

		final DownloadTask downloadTask = new DownloadTask(MainActivity.this);

		downloadTask.execute("http://www.flamingtext.com/net-fu/image_output.cgi?script="
				+ modes.getSelectedItem().toString().replaceAll(" ", "-").toLowerCase() + "&text="
				+ text.getText().toString() + "&fontsize=200&fontname="
				+ font.getSelectedItem().toString().replaceAll(" ", "+") + "&fillTextColor=%23"
				+ getcolor(inner.getSelectedItem().toString()) + "&fillOutlineColor=%23"
				+ getcolor(glow.getSelectedItem().toString()) + "&frameRate=50&backgroundColor=%23"
				+ getcolor(back.getSelectedItem().toString()) + "#customize.html");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
		}
		while (!pagetxt.exists()) {

		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/page"));
			final String page = br.readLine();
			pagetxt.delete();
			br.close();
			int str, end;
			if (page.contains("http")) {
				int i = page.indexOf("http");
				if (page.contains(".gif") || page.contains(".png")) {
					int j = page.indexOf(".gif");
					if (j == -1) {
						j = page.indexOf(".png");
					}
					end = j + 4;
					str = i;

					File resfile = new File(Environment.getExternalStorageDirectory(), "/res");

					String url = page.substring(str, end);

					DownloadTask2 downloadTask2 = new DownloadTask2(MainActivity.this);
					downloadTask2.execute(url);

					Thread.sleep(3000);
					// wb.loadUrl(url);

					if (ew2.exists() || ew3.exists()) {
						try {
							WebView wv = (WebView) findViewById(R.id.webView1);
							if (page.contains(".gif")) {
								wb.setVisibility(View.INVISIBLE);
								wv.setVisibility(View.VISIBLE);
								String x = "<!DOCTYPE html><html><body><img src=\"" + page.substring(str, end)
										+ "\" alt=\"Smileyface\" width=\"100%\" height=\"100%\"></body></html>";

								wv.loadData(x, "text/html", "utf-8");
							} else {
								wb.setVisibility(View.VISIBLE);
								wv.setVisibility(View.INVISIBLE);
								Bitmap bmp = BitmapFactory.decodeFile(ew3.toString());
								wb.setImageBitmap(bmp);
							}
						} catch (Exception e) {

						}
					} else if (!res.exists()) {
						wb.setImageResource(R.drawable.faild);
					}
					text.setText(text.getText().toString().replaceAll("%20", " "));

				}

			}

		} catch (Exception e) {
			String problem = e.toString();

		}

	}
}

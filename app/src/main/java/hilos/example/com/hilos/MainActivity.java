package hilos.example.com.hilos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText entrada;
    private TextView salida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrada = (EditText) findViewById(R.id.entrada);
        salida = (TextView) findViewById(R.id.salida);
    }
    /*
    // 5_1
    public void calcularOperacion(View view) {
        int n = Integer.parseInt(entrada.getText().toString());
        salida.append(n + "! = ");
        int res = factorial(n);
        salida.append(res +"\n");
    }
    */
    public void calcularOperacion(View view) {
        int n = Integer.parseInt(entrada.getText().toString());
        salida.append(n + "! = ");
        /*
        MiThread thread = new MiThread(n);
        thread.start();
        */
        // 5_4
        MiTarea tarea = new MiTarea();
        tarea.execute(n);
    }
    public int factorial(int n) {
        int res = 1;
        for (int i = 1; i <= n; i++){
            res *= i;
            SystemClock.sleep(1000);
        }
        return res;
    }

    // 5_2
    class MiThread extends Thread {
        private int n, res;
        public MiThread(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            res = factorial(n);
            /*
            // 5_2
            salida.append(res + "\n");
            */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    salida.append(res + "\n");
                }
            });
        }
    }

    /*
    // 5_4
    class MiTarea extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... n) {
            return factorial(n[0]);
        }
        @Override
        protected void onPostExecute(Integer res) {
            salida.append(res + "\n");
        }
    }
    */

    // 5_5
    class MiTarea extends AsyncTask<Integer, Integer, Integer> {
        private ProgressDialog progreso;
        @Override
        protected void onPreExecute() {
            progreso = new ProgressDialog(MainActivity.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progreso.setMessage("Calculando...");
            // progreso.setCancelable(false);
            // 5_6
            progreso.setCancelable(true);
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MiTarea.this.cancel(true);
                }
            });


            progreso.setMax(100);
            progreso.setProgress(0);
            progreso.show();
        }
        @Override
        protected Integer doInBackground(Integer... n) {
            Integer progreso = new Integer(0);
            int res = 1;
            // for (int i = 1; i <= n[0]; i++) {

            // 5_6
            for (int i = 1; i <= n[0] && !isCancelled(); i++) {
                res *= i;
                SystemClock.sleep(1000);
                progreso = (i * 100) / n[0];
                publishProgress(progreso);
            }
            return res;
        }
        @Override
        protected void onProgressUpdate(Integer... porcentaje) {
            progreso.setProgress(porcentaje[0]);
        }
        @Override
        protected void onPostExecute(Integer res) {
            progreso.dismiss();
            salida.append(res + "\n");
        }
        // 5_6
        @Override
        protected void onCancelled() {
            salida.append("cancelado\n");
        }

    }
}

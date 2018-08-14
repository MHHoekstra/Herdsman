package br.uepg.projeto.herdsman;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Parto;

public class CadastroPartoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Parto parto;
    Animal animal;
    Button data;
    int dia;
    int mes;
    int ano;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_parto);
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);
        animal = (Animal) getIntent().getSerializableExtra("Animal");
        data = (Button) findViewById(R.id.cadastro_parto_button_setDate);
        data.setText(String.valueOf(dia) + "/" + String.valueOf(mes)+"/"+String.valueOf(ano));
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.cadastro_parto_radio_group);
        TextView titulo = (TextView) findViewById(R.id.cadastro_parto_titulo);
        FloatingActionButton cancelar = (FloatingActionButton) findViewById(R.id.cadastro_parto_cancelar);
        FloatingActionButton cadastrar = (FloatingActionButton)findViewById(R.id.cadastro_parto_done);
        String s = "Cadastrar parto de " + animal.getNumero();
        titulo.setText(s);

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "Data");
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tipoCria = 1;
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.cadastro_parto_radio_femea:
                    {
                        tipoCria = 1;
                        break;
                    }

                    case R.id.cadastro_parto_radio_macho:
                    {
                        tipoCria = 2;
                        break;
                    }

                    case R.id.cadastro_parto_radio_gemeos_femea:
                    {
                        tipoCria = 3;
                        break;
                    }

                    case R.id.cadastro_parto_radio_gemeos_macho:
                    {
                        tipoCria = 4;
                        break;
                    }

                    case R.id.cadastro_parto_radio_macho_femea:
                    {
                        tipoCria = 5;
                        break;
                    }
                }

                parto = new Parto(animal.getId(), tipoCria,data.getText().toString(), ano + "-" + mes + "-" + dia);
                HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroPartoActivity.this);
                long insert = mDbHelper.inserirParto(parto);
                if (insert > 0) {
                    Toast.makeText(CadastroPartoActivity.this, "Parto cadastrado", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        data.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month)+"/"+String.valueOf(year));
        dia = dayOfMonth;
        mes = month;
        ano = year;
    }
}

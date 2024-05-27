package com.example.teacherhub.util.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import com.example.teacherhub.models.Course;

import java.util.ArrayList;

public class AlertHelper {

        public static void SimpleAlert(Context context, String title, String mensaje, String textoBoton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setMessage(mensaje)
                    .setPositiveButton(textoBoton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public static void SimpleAlert2(Context context, String title, String mensaje, String textoBotonAceptar, String textoBotonCancelar, final OnConfirmationListener listener) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setMessage(mensaje)
                    .setPositiveButton(textoBotonAceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onConfirm();
                        }
                    })
                    .setNegativeButton(textoBotonCancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public static void InputAlert(Context context, String title, String hint, String textoBotonPositivo, String textoBotonNegativo, final OnInputListener listener) {
            final EditText input = new EditText(context);
            input.setHint(hint);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setView(input)
                    .setPositiveButton(textoBotonPositivo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String inputValue = input.getText().toString().trim();
                            listener.onInput(inputValue);
                        }
                    })
                    .setNegativeButton(textoBotonNegativo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public static void InputAlert3(Context context, String title, String hint1,String InputText1, String hint2,String InputText2, String hint3,String clase, String textoBotonPositivo, String textoBotonNegativo, final OnTripleInputListener listener) {
            final EditText input1 = new EditText(context);
            final EditText input2 = new EditText(context);
            final EditText input3 = new EditText(context);
            input1.setHint(hint1);
            input2.setHint(hint2);
            input3.setHint(hint3);

            input1.setText(InputText1);
            input2.setText(InputText2);

            if(clase.equals("Password")){
                input3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(input1);
            layout.addView(input2);
            layout.addView(input3);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setView(layout)
                    .setPositiveButton(textoBotonPositivo, null)
                    .setNegativeButton(textoBotonNegativo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputValue1 = input1.getText().toString().trim();
                    String inputValue2 = input2.getText().toString().trim();
                    String inputValue3 = input3.getText().toString().trim();

                    boolean hasErrors = Empty.checkEmptyField(input1) ||
                            Empty.checkEmptyField(input2) ||
                            Empty.checkEmptyField(input3);

                    if(!hasErrors){
                        listener.onTripleInput(inputValue1, inputValue2, inputValue3);
                        dialog.dismiss();
                    }
                }
            });
        }

    public static void DropdownAlert(Context context, String titulo, ArrayList<Course> cursos, String textoBotonPositivo, String textoBotonNegativo, final OnDropdownSelectedListener listener) {
        final Spinner spinner = new Spinner(context);

        ArrayList<String> nombresCursos = new ArrayList<>();
        for (Course curso : cursos) {
            nombresCursos.add(curso.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, nombresCursos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setView(spinner)
                .setPositiveButton(textoBotonPositivo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = spinner.getSelectedItemPosition();
                        if (selectedPosition != AdapterView.INVALID_POSITION) {
                            Course cursoSeleccionado = cursos.get(selectedPosition);
                            listener.onDropdownSelected(cursoSeleccionado.getId());
                        }
                    }
                })
                .setNegativeButton(textoBotonNegativo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public interface OnConfirmationListener {
            void onConfirm();
        }
        public interface OnInputListener {
            void onInput(String input);
        }
        public interface OnTripleInputListener {
            void onTripleInput(String input1, String input2, String input3);
        }
        public interface OnDropdownSelectedListener {
            void onDropdownSelected(String selectedOption);
        }
    }


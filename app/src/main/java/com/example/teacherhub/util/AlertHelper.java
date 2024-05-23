package com.example.teacherhub.util;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;

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

        public static void InputAlert3(Context context, String title, String hint1, String hint2, String hint3, String textoBotonPositivo, String textoBotonNegativo, final OnTripleInputListener listener) {
            final EditText input1 = new EditText(context);
            final EditText input2 = new EditText(context);
            final EditText input3 = new EditText(context);
            input1.setHint(hint1);
            input2.setHint(hint2);
            input3.setHint(hint3);

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(input1);
            layout.addView(input2);
            layout.addView(input3);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setView(layout)
                    .setPositiveButton(textoBotonPositivo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String inputValue1 = input1.getText().toString().trim();
                            String inputValue2 = input2.getText().toString().trim();
                            String inputValue3 = input3.getText().toString().trim();
                            listener.onTripleInput(inputValue1, inputValue2, inputValue3);
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

        public static void DropdownAlert(Context context, String titulo, String[] opciones, String textoBotonPositivo, String textoBotonNegativo, final OnDropdownSelectedListener listener) {
            final Spinner spinner = new Spinner(context);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, opciones);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo)
                    .setView(spinner)
                    .setPositiveButton(textoBotonPositivo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selectedOption = (String) spinner.getSelectedItem();
                            listener.onDropdownSelected(selectedOption);
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


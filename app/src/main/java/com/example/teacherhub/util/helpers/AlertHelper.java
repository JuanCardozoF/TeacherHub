package com.example.teacherhub.util.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.teacherhub.R;
import com.example.teacherhub.models.Course;
import com.example.teacherhub.models.User;

import java.util.ArrayList;
import java.util.Objects;

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

        public static void InputAlert(Context context, String title, String hint, String textDescription, String buttonPositive, final OnInputListener listener) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View alertLayout = inflater.inflate(R.layout.inputalert, null);

            TextView headerTitle = alertLayout.findViewById(R.id.headerTitle);
            headerTitle.setText(title);

            EditText courseNameInput = alertLayout.findViewById(R.id.courseNameInput);
            courseNameInput.setHint(hint);

            TextView description = alertLayout.findViewById(R.id.description);
            description.setText(textDescription);

            ImageButton closeButton = alertLayout.findViewById(R.id.closeButton);
            Button exitButton = alertLayout.findViewById(R.id.exitButton);
            Button createButton = alertLayout.findViewById(R.id.createButton);
            createButton.setText(buttonPositive);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(alertLayout);

            AlertDialog dialog = builder.create();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputValue = courseNameInput.getText().toString().trim();
                    listener.onInput(inputValue);
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        public static void InputAlert3(Context context, String title, String hint1, String InputText1, String hint2,String InputText2, String hint3,String clase, String textoBotonPositivo, final OnTripleInputListener listener) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View alertLayout = inflater.inflate(R.layout.input3alert, null);

            TextView headerTitle = alertLayout.findViewById(R.id.headerTitle);
            headerTitle.setText(title);

            EditText emailInput = alertLayout.findViewById(R.id.emailInput);
            emailInput.setHint(hint1);
            emailInput.setText(InputText1);

            EditText usernameInput = alertLayout.findViewById(R.id.usernameInput);
            usernameInput.setHint(hint2);
            usernameInput.setText(InputText2);

            EditText roleInput = alertLayout.findViewById(R.id.roleInput);
            roleInput.setHint(hint3);

            if (clase.equals("Password")) {
                roleInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                roleInput.setText("Password");
            } else if(clase.equals("Password2")){

                TextView emial = alertLayout.findViewById(R.id.textNP);
                TextView passwd = alertLayout.findViewById(R.id.txtep);
                TextView conPassword = alertLayout.findViewById(R.id.textrp);
                emial.setText("nueva Contraseña");
                passwd.setText("confirmar contraseña");
                conPassword.setText("Codigo");
                emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                usernameInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            } else {
                roleInput.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            ImageButton closeButton = alertLayout.findViewById(R.id.closeButton);
            Button exitButton = alertLayout.findViewById(R.id.exitButton);
            Button createButton = alertLayout.findViewById(R.id.createButton);
            createButton.setText(textoBotonPositivo);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(alertLayout);

            AlertDialog dialog = builder.create();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputValue1 = emailInput.getText().toString().trim();
                    String inputValue2 = usernameInput.getText().toString().trim();
                    String inputValue3 = roleInput.getText().toString().trim();

                    boolean hasErrors = Empty.checkEmptyField(emailInput) ||
                            Empty.checkEmptyField(usernameInput) ||
                            Empty.checkEmptyField(roleInput);

                    if (!hasErrors) {
                        if (clase.equals("Password2")) {
                            if (emailInput.getText().toString().equals(usernameInput.getText().toString())) {
                                listener.onTripleInput(inputValue1, inputValue2, inputValue3);
                                dialog.dismiss();
                            } else {
                                usernameInput.setError("No coinciden");
                            }
                        } else {
                            listener.onTripleInput(inputValue1, inputValue2, inputValue3);
                            dialog.dismiss();
                        }
                    }
                }
            });

            dialog.show();
        }

    public static void DropdownAlert(Context context, String titulo, ArrayList<Course> cursos, String textoBotonPositivo, String textoBotonNegativo, final OnDropdownSelectedListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout = inflater.inflate(R.layout.inputalert_spinner, null);

        TextView headerTitle = alertLayout.findViewById(R.id.headerTitle);
        headerTitle.setText(titulo);

        Spinner classSpinner = alertLayout.findViewById(R.id.classSpinner);

        ArrayList<String> nombresCursos = new ArrayList<>();
        for (Course curso : cursos) {
            nombresCursos.add(curso.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, nombresCursos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        ImageButton closeButton = alertLayout.findViewById(R.id.closeButton);
        Button exitButton = alertLayout.findViewById(R.id.exitButton);
        Button createButton = alertLayout.findViewById(R.id.createButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(alertLayout);

        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = classSpinner.getSelectedItemPosition();
                if (selectedPosition != AdapterView.INVALID_POSITION) {
                    Course cursoSeleccionado = cursos.get(selectedPosition);
                    listener.onDropdownSelected(cursoSeleccionado.getId());
                    dialog.dismiss();
                }
            }
        });

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


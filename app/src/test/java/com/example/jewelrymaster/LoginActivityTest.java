package com.example.jewelrymaster;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    @Mock
    private EditText mockEditTextEmail;

    @Mock
    private EditText mockEditTextPassword;

    @Mock
    private Button mockButtonLogin;

    @Mock
    private TextView mockTextViewCreateAccount;

    @Mock
    private FirebaseAuth mockFirebaseAuth;

    @Mock
    private Task<AuthResult> mockAuthResultTask;

    private LoginActivity loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        loginActivity = spy(new LoginActivity());
        doReturn(mockEditTextEmail).when(loginActivity).findViewById(R.id.editTextEmail);
        doReturn(mockEditTextPassword).when(loginActivity).findViewById(R.id.editTextPassword);
        doReturn(mockButtonLogin).when(loginActivity).findViewById(R.id.buttonLogin);
        doReturn(mockTextViewCreateAccount).when(loginActivity).findViewById(R.id.textViewCreateAccount);
        doReturn(mockFirebaseAuth).when(mockFirebaseAuth).getInstance(); // Mocking the FirebaseAuth.getInstance() method
    }
    @Test
    public void testButtonLoginOnClick_WithEmptyEmail_ShouldSetError() {
        // Arrange
        String emptyEmail = "";
        doReturn(emptyEmail).when(mockEditTextEmail).getText().toString();

        // Act
        mockButtonLogin.performClick();

        // Assert
        verify(mockEditTextEmail).setError("Введите email");
        verify(mockFirebaseAuth, never()).signInWithEmailAndPassword(anyString(), anyString());
        verify(loginActivity, never()).startActivity(any(Intent.class));
    }

    @Test
    public void testButtonLoginOnClick_WithEmptyPassword_ShouldSetError() {
        // Arrange
        String emptyPassword = "";
        doReturn(emptyPassword).when(mockEditTextPassword).getText().toString();

        // Act
        mockButtonLogin.performClick();

        // Assert
        verify(mockEditTextPassword).setError("Введите пароль");
        verify(mockFirebaseAuth, never()).signInWithEmailAndPassword(anyString(), anyString());
        verify(loginActivity, never()).startActivity(any(Intent.class));
    }

    @Test
    public void testButtonLoginOnClick_WithValidInput_ShouldStartMainActivity() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        doReturn(email).when(mockEditTextEmail).getText().toString();
        doReturn(password).when(mockEditTextPassword).getText().toString();
        when(mockFirebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockAuthResultTask);
        doNothing().when(loginActivity).startActivity(any(Intent.class));

        // Act
        mockButtonLogin.performClick();

        // Assert
        verify(mockEditTextEmail, never()).setError(anyString());
        verify(mockEditTextPassword, never()).setError(anyString());
        verify(mockFirebaseAuth).signInWithEmailAndPassword(email, password);
        verify(loginActivity).startActivity(any(Intent.class));
        verify(loginActivity).finish();
    }

    @Test
    public void testTextViewCreateAccountOnClick_ShouldStartRegisterActivity() {
        // Act
        mockTextViewCreateAccount.performClick();

        // Assert
        verify(loginActivity).startActivity(any(Intent.class));
    }
}
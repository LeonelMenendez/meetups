import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { ISignInRequest } from 'src/app/shared/models/auth';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
})
export class SignInComponent implements OnInit {
  form: FormGroup;
  hidePassword: boolean = true;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  get email(): AbstractControl {
    return this.form.controls.email;
  }

  get password(): AbstractControl {
    return this.form.controls.password;
  }

  get emailError(): string {
    if (this.email.hasError('required')) {
      return 'The email is required';
    } else if (this.email.hasError('email')) {
      return 'The email is not valid';
    }
  }

  get passwordError(): string {
    if (this.password.hasError('required')) {
      return 'The password is required';
    }
  }

  submit() {
    if (!this.form.valid) {
      return;
    }

    this.authService.signIn(this.signInRequest).subscribe((res) => {
      this.router.navigate(['/home']);
    });
  }

  private get signInRequest(): ISignInRequest {
    return {
      email: this.email.value,
      password: this.password.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth.service';
import { FormValidatorService } from 'src/app/core/services/form-validator.service';
import { ISignUpRequest } from 'src/app/shared/models/auth';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss'],
})
export class SignUpComponent implements OnInit {
  form: FormGroup;
  hidePassword: boolean = true;
  hideConfirmPassword: boolean = true;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private formValidator: FormValidatorService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group(
      {
        name: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required],
        confirmPassword: ['', Validators.required],
        admin: [],
      },
      { validator: this.formValidator.misMatch('password', 'confirmPassword') }
    );
  }

  get name(): AbstractControl {
    return this.form.controls.name;
  }

  get email(): AbstractControl {
    return this.form.controls.email;
  }

  get password(): AbstractControl {
    return this.form.controls.password;
  }

  get confirmPassword(): AbstractControl {
    return this.form.controls.confirmPassword;
  }

  get admin(): AbstractControl {
    return this.form.controls.admin;
  }

  get nameError(): string {
    if (this.name.hasError('required')) {
      return 'The name is required';
    }
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

  get confirmPasswordError(): string {
    if (this.confirmPassword.hasError('required')) {
      return 'The confirm password is required.';
    } else if (this.confirmPassword.hasError('misMatch')) {
      return 'The passwords do not match';
    }
  }

  submit() {
    if (!this.form.valid) {
      return;
    }

    this.authService.signUp(this.signUpRequest).subscribe((res) => {
      this.toastr.success('User created successfully');
      this.router.navigate(['../signin']);
    });
  }

  private get signUpRequest(): ISignUpRequest {
    return {
      name: this.name.value,
      email: this.email.value,
      password: this.password.value,
      admin: this.admin.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { ISignInRequest } from 'src/app/shared/models/auth';
import { IUser } from 'src/app/shared/models/user';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
})
export class SignInComponent implements OnInit {
  form: FormGroup;
  hidePassword: boolean = true;
  returnUrl: string;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    if (this.authService.currentUserValue) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
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

    this.authService.signIn(this.signInRequest).subscribe((user: IUser) => {
      this.authService.currentUserValue = user;
      this.router.navigate([this.returnUrl]);
    });
  }

  private get signInRequest(): ISignInRequest {
    return {
      email: this.email.value,
      password: this.password.value,
    };
  }
}

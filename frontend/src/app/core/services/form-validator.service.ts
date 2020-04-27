import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class FormValidatorService {
  misMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors.misMatch) {
        return;
      }

      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ misMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
    };
  }
}

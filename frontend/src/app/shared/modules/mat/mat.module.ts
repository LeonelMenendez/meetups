import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@NgModule({
  declarations: [],
  imports: [MatButtonModule, MatIconModule, MatInputModule, MatSlideToggleModule],
  exports: [MatButtonModule, MatIconModule, MatInputModule, MatSlideToggleModule],
})
export class MatModule {}

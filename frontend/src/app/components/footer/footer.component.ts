import {Component, OnInit} from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {ThemePicker} from '../theme-picker/theme-picker';
import {MatButtonModule} from '@angular/material/button';
import {AppThemeModule} from '../../app-theme.module';

@Component({
  standalone: true,
  selector: 'app-footer',
  imports: [
    MatIconModule,
    ThemePicker,
    MatButtonModule,
    AppThemeModule,
  ],
  templateUrl: './footer.component.html',
})
export class FooterComponent implements OnInit {

  constructor() {
    //
  }

  ngOnInit(): void {
  }

}

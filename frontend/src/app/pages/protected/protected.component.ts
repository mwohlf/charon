import {Component, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';
import {PageComponent} from '../../components/page/page.component';
import {MatCardModule} from '@angular/material/card';
import {AsyncPipe, NgIf} from '@angular/common';

@Component({
  imports: [
    PageComponent,
    MatCardModule,
    NgIf,
    AsyncPipe,
  ],
  standalone: true,
  selector: 'app-protected',
  templateUrl: './protected.component.html',
  styleUrls: ['./protected.component.scss'],
})
export class ProtectedComponent implements OnInit {

  static SPEC: PageConfig = {
    route: 'protected',
    icon: 'loop',
    title: 'Protected',
    requiredRoles: [],
    component: ProtectedComponent,
  };

  constructor() {
  }

  ngOnInit(): void {
  }

}

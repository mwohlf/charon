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
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {

  static SPEC: PageConfig = {
    route: 'main',
    icon: 'stars',
    title: 'Main',
    requiredRoles: [],
    component: MainComponent,
  };

  constructor() {
  }

  ngOnInit(): void {
  }

}

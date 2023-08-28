import {Component, Input, OnInit} from '@angular/core';

@Component({
  imports: [],
  selector: 'app-page',
  standalone: true,
  templateUrl: './page.component.html',
})
export class PageComponent implements OnInit {

  @Input() header: string = '';

  constructor() {
    //
  }

  ngOnInit(): void {
  }

}

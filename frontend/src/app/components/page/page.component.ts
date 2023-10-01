import {Component, HostBinding, Input, OnInit} from '@angular/core';

@Component({
  imports: [],
  selector: 'app-page',
  standalone: true,
  templateUrl: './page.component.html',
})
export class PageComponent implements OnInit {

  @HostBinding('class') class = 'main-content';

  @Input() header: string = '';

  constructor() {
    //
  }

  ngOnInit(): void {
  }

}

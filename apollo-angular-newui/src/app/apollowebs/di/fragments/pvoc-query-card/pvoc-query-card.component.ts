import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-pvoc-query-card',
  templateUrl: './pvoc-query-card.component.html',
  styleUrls: ['./pvoc-query-card.component.css']
})
export class PvocQueryCardComponent implements OnInit {
  @Input()
  queryDetails: any

  constructor() {
  }

  ngOnInit(): void {
  }

}

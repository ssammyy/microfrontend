import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-consignment-status',
  templateUrl: './consignment-status.component.html',
  styleUrls: ['./consignment-status.component.css']
})
export class ConsignmentStatusComponent implements OnInit {

  @Input() value: string
  constructor() { }

  ngOnInit(): void {
  }

}

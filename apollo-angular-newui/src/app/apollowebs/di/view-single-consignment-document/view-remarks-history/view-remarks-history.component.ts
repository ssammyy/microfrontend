import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-view-remarks-history',
  templateUrl: './view-remarks-history.component.html',
  styleUrls: ['./view-remarks-history.component.css']
})
export class ViewRemarksHistoryComponent implements OnInit {
  @Input() remarks: any[]
  constructor() { }

  ngOnInit(): void {
  }
}

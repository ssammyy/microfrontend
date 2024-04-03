import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent  {
  chart: any;
  items: any[];
  showList: boolean = true
  showOutOfStock: boolean = true
  constructor() {
    this.items = new Array(5)
  }


  dashBoardInfo: { description: string, number: number, backgroundColor: string }[] = [
    { description: 'Today Sales', number: 42, backgroundColor: '#DDBEAA' },
    { description: 'Total Profit', number: 72400, backgroundColor: '#E5E3E4' },
    { description: 'Outstanding dues', number: 33000, backgroundColor: '#BBC6C8' }
  ];







}

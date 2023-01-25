import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-smark-admin',
  templateUrl: './smark-admin.component.html',
  styleUrls: ['./smark-admin.component.css']
})
export class SmarkAdminComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  id:any ="My Tasks";
  tabChange(ids:any){
    this.id=ids;
    console.log(this.id);
  }


}

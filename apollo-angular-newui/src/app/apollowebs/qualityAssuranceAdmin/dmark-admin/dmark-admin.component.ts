import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dmark-admin',
  templateUrl: './dmark-admin.component.html',
  styleUrls: ['./dmark-admin.component.css']
})
export class DmarkAdminComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  
  id:any ="My Tasks";
  tabChange(ids:any){
    this.id=ids;
    console.log(this.id);
  }


}

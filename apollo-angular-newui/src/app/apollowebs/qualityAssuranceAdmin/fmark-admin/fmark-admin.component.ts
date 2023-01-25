import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-fmark-admin',
  templateUrl: './fmark-admin.component.html',
  styleUrls: ['./fmark-admin.component.css']
})
export class FmarkAdminComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  id:any ="My Tasks";
  tabChange(ids:any){
    this.id=ids;
    console.log(this.id);
  }

}

import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-domestic-notification',
  templateUrl: './domestic-notification.component.html',
  styleUrls: ['./domestic-notification.component.css']
})
export class DomesticNotificationComponent implements OnInit {

  displayedColumns: string[] = ['snNumber', 'notifocationSymbol', 'standardNumber', 'circulationDate', 'tcSecretary', 'title'];

  searchFormGroup!: FormGroup;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
//  dataSource!: MatTableDataSource<>;

  constructor(
    private formBuilder: FormBuilder,
   ) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
      title: ['', null],
      circulationDate: ['', null],
     
    });


  }

} 


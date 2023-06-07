import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-standard-work-programme-bulletin',
  templateUrl: './standard-work-programme-bulletin.component.html',
  styleUrls: ['./standard-work-programme-bulletin.component.css']
})
export class StandardWorkProgrammeBulletinComponent implements OnInit {

  displayedColumns: string[] = ['technicalCommittee', 'total', 'initialStage', 'stageOne', 'stageTwo', 'stageThree','stageFour', 'stageFive', 'stageSix'];

  searchFormGroup!: FormGroup;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  // dataSource!: MatTableDataSource<>;

  constructor(
    private formBuilder: FormBuilder,
   ) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
     
    });


  }

} 

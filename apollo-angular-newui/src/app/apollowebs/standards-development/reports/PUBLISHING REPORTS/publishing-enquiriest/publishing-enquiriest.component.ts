import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-publishing-enquiriest',
  templateUrl: './publishing-enquiriest.component.html',
  styleUrls: ['./publishing-enquiriest.component.css']
})
export class PublishingEnquiriestComponent implements OnInit {

  displayedColumns: string[] = ['projectId','enquiry','requester','dateReceived','deliverables','dateCompleted','comment'];

  searchFormGroup!: FormGroup;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
//  dataSource!: MatTableDataSource<>;

  constructor(private store$: Store<any>,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private SpinnerService: NgxSpinnerService,) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
      dateReceived: ['', null],
      dateCompleted: ['', null],
      
    });


  }

}


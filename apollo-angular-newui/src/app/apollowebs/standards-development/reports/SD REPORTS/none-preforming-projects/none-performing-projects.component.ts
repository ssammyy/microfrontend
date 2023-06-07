import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';
import { OverlayService } from 'src/app/shared/loader/overlay.service';

@Component({
  selector: 'app-none-performing-projects',
  templateUrl: './none-performing-projects.component.html',
  styleUrls: ['./none-performing-projects.component.css']
})
export class NonePerformingProjectsComponent implements OnInit {

  displayedColumns: string[] = ['projectId', 'currentStage', 'projectedStage', 'noOfWeeksDelayed','delayJustification','comments'];

  searchFormGroup!: FormGroup;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  // dataSource!: MatTableDataSource<>;

  constructor(private store$: Store<any>,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private SpinnerService: NgxSpinnerService,
              private spinnerService: OverlayService) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
      currentStage: ['', null],
      projectedStage: ['', null],
    });


  }
  applyFilter(formDirective): void {
    this.spinnerService.show("Filtering Data")
  }

}

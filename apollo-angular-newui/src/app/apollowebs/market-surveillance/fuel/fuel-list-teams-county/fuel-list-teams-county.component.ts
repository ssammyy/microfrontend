import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  FuelEntityDto, FuelInspectionDto, FuelScheduleCountyListDetailsDto,
  FuelScheduleTeamsListDetailsDto,
  MsUsersDto,
  TeamsCountyFuelSaveDto,
  TeamsFuelSaveDto,
} from '../../../../core/store/data/ms/ms.model';
import {Observable, Subject} from 'rxjs';
import {County, CountyService, selectUserInfo, Town, TownService} from '../../../../core/store';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';

@Component({
  selector: 'app-fuel-list-teams-county',
  templateUrl: './fuel-list-teams-county.component.html',
  styleUrls: ['./fuel-list-teams-county.component.css'],
})
export class FuelListTeamsCountyComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  currDiv!: string;
  currDivLabel!: string;
  addNewScheduleForm!: FormGroup;
  createTeamsForm!: FormGroup;
  createTeamsCountyForm!: FormGroup;
  closeBatchForm!: FormGroup;
  dataSave: FuelEntityDto;
  submitted = false;
  selectedBatchRefNo: string;
  selectedTeamRefNo: string;
  selectedCounty: string;
  selectedTown = 0;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  loading = false;

  roles: string[];

  minDate = new Date();

  activeStatus = 'my-tasks';
  previousStatus = 'my-tasks';
  searchStatus: any;
  personalTasks = 'false';
  defaultPageSize = 20;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;
  totalCount = 12;
  public settings = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'},
      ],
      position: 'right', // left|right
    },
    rowClassFunction: (row) => {
      // console.log(row)
      if (row.data.isNcrDocument) {
        return 'risky';
      } else {
        return '';
      }

    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      // id: {
      //   title: 'ID',
      //   type: 'string',
      //   filter: false
      // },
      referenceNumber: {
        title: 'REFERENCE NUMBER',
        type: 'string',
        filter: false,
      },
      countyName: {
        title: 'COUNTY NAME',
        type: 'string',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  dataSet: LocalDataSource = new LocalDataSource();
  documentTypes: any[];
  message: any;
  keywords: any;
  private documentTypeUuid: string;
  private documentTypeId: any;
  epraUser = false;
  managerPetroleumUser = false;
  ioUser = false;
  search: Subject<string>;
  loadedData: FuelScheduleCountyListDetailsDto = new FuelScheduleCountyListDetailsDto;
  officersList: MsUsersDto[];
  dataSaveTeams: TeamsFuelSaveDto;
  dataSaveTeamsCounty: TeamsCountyFuelSaveDto;
  dataSaveTeamsCountyFuelList: TeamsCountyFuelSaveDto[] = [];




  constructor(
      private store$: Store<any>,
      // private dialog: MatDialog,
      private activatedRoute: ActivatedRoute,
      private router: Router,
      private formBuilder: FormBuilder,
      private SpinnerService: NgxSpinnerService,
      private countyService: CountyService,
      private townService: TownService,
      private msService: MsService,
  ) {
    this.county$ = countyService.entities$;
    this.town$ = townService.entities$;
    countyService.getAll().subscribe();
  }

  ngOnInit(): void {

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.roles = u.roles;
    });

    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.selectedBatchRefNo = rs.get('batchReferenceNumber');
          this.selectedTeamRefNo = rs.get('teamsReferenceNo');
          this.loadData(this.selectedBatchRefNo, this.selectedTeamRefNo, this.defaultPage, this.defaultPageSize);
        },
    );



    this.createTeamsForm = this.formBuilder.group({
      teamName: ['', Validators.required],
      assignedOfficerID: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      remarks: null,
    });

    this.createTeamsCountyForm = this.formBuilder.group({
      countyId: ['', Validators.required],
      remarks: null,
    });

    this.closeBatchForm = this.formBuilder.group({
      remarks: ['', Validators.required],
    });
  }

  goBack() {
    this.router.navigate(['/epra', this.selectedBatchRefNo, this.selectedTeamRefNo]);
  }

  private loadData(batchReferenceNumber: string, teamsReferenceNo: string, page: number, records: number): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    // this.totalCount = this.loadedData.fuelInspectionDto.length;
    // this.dataSet.load(this.loadedData.fuelInspectionDto);
    // this.SpinnerService.hide();
    this.msService.msFuelInspectionTeamCountyList(batchReferenceNumber, teamsReferenceNo, String(page), String(records)).subscribe(
        (data) => {
          this.loadedData = data;
          this.totalCount = this.loadedData.fuelCountyDtoList.length;
          this.dataSet.load(this.loadedData.fuelCountyDtoList);
          this.SpinnerService.hide();
          console.log(data);
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }


  onManagerPetroleumChange(event: any) {
    if (this.managerPetroleumUser) {
      this.personalTasks = event.target.value;
      this.loadData(this.selectedBatchRefNo, this.selectedTeamRefNo, this.defaultPage, this.defaultPageSize);
    }
  }

  toggleStatus(status: string): void {
    console.log(status);
    this.message = null;
    this.searchStatus = null;
    if (status !== this.activeStatus) {
      this.activeStatus = status;
      this.loadData(this.selectedBatchRefNo, this.selectedTeamRefNo, this.defaultPage, this.defaultPageSize);
    }
  }

  public onCustomAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewRecord(event.data);
        break;
    }
  }

  viewRecord(data: FuelInspectionDto) {
    console.log('TEST 101 REF NO: ' + data.referenceNumber);
    // details/:referenceNumber/teamsReferenceNo
    this.router.navigate([`/epra/details/`,  this.selectedBatchRefNo, this.selectedTeamRefNo, data.referenceNumber]);
  }

  get formNewScheduleForm(): any {
    return this.addNewScheduleForm.controls;
  }

  get formCreateTeamsForm(): any {
    return this.createTeamsForm.controls;
  }

  get formCreateTeamsCountyForm(): any {
    return this.createTeamsCountyForm.controls;
  }

  get formCloseBatchForm(): any {
    return this.closeBatchForm.controls;
  }


  updateSelectedCounty() {
    let countyDetail: County;
    this.county$.subscribe(event => {
      countyDetail = event.find(item => item.id === this.createTeamsCountyForm?.get('countyId')?.value);
    });
    this.selectedCounty = countyDetail.county;
  }

  updateSelectedTown() {
    this.selectedTown = this.addNewScheduleForm?.get('town')?.value;
    console.log(`town set to ${this.selectedTown}`);
  }


  openModalAddDetails(divVal: string): void {
    const arrHead = ['addNewScheduleDetails', 'closeBatchDetails'];
    const arrHeadSave = ['ADD NEW SCHEDULE DETAILS', 'CLOSE BATCH AND SEND TO KEBS'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }
    this.currDiv = divVal;
  }


  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
      this.loadData(this.selectedBatchRefNo, this.selectedTeamRefNo, this.currentPageInternal, this.defaultPageSize);
    }
  }

  // Remove Form repeater values
  removeDataCountyInTeams(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveTeamsCountyFuelList.splice(index, 1);
    } else {
      this.dataSaveTeamsCountyFuelList.splice(index, index);
    }
  }

}


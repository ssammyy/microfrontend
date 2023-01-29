import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {TivetEntity, UserEntityDto, UserEntityService} from "../../../core/store";
import {Observable, Subject} from "rxjs";
import {Titles, TitlesService} from "../../../core/store/data/title";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserRegister} from "../../../shared/models/user";
import {
  Counties, Department,
  DesignationEntityDto,
  DirectoratesEntityDto, DivisionDetails,
  RegionsEntityDto, SectionsEntityDto, SubSectionsL1EntityDto, SubSectionsL2EntityDto, TitlesEntityDto, Towns
} from "../../../shared/models/master-data-details";
import {DataTableDirective} from "angular-datatables";
import {Store} from "@ngrx/store";
import {LoadingService} from "../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {MasterService} from "../../../core/store/data/master/master.service";
import {ActivatedRoute, Router} from "@angular/router";
import swal from "sweetalert2";
import Swal from "sweetalert2";

class DataTable {
}

@Component({
  selector: 'app-manage-tivet',
  templateUrl: './manage-tivet.component.html',
  styleUrls: ['./manage-tivet.component.css']
})
export class ManageTivetComponent implements OnInit {

  user: TivetEntity;

  tasks: TivetEntity[] = [];
  public actionRequest: TivetEntity | undefined;

  updateTivetFormGroup: FormGroup = new FormGroup({});
  public dataTable!: DataTable;

  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  public itemId: number;
  loadingText: string;

  constructor(private store$: Store<any>,
              private _loading: LoadingService,
              private SpinnerService: NgxSpinnerService,
              private service: UserEntityService,
              private masterService: MasterService,
              private titleService: TitlesService,
              private router: Router,
              private route: ActivatedRoute,

              private formBuilder: FormBuilder) {
  }

  get formAddUser(): any {
    return this.updateTivetFormGroup.controls;
  }

  ngOnInit(): void {
    this.loadingText = "Retrieving Data Please Wait ...."
    this.SpinnerService.show();
    let formattedArray = [];
    this.SpinnerService.show();
    this.masterService.loadTivets().subscribe(
        (data: any) => {
          this.SpinnerService.hide();
          this.tasks = data;

          formattedArray = data.map(i => [i.id, i.firstName, i.lastName, i.userName, i.email, i.registrationDate, i.status]);
          this.SpinnerService.hide();
          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
          }

        });



    this.updateTivetFormGroup = this.formBuilder.group({
      id: ['', Validators.required],

    });

  }


  onClickSave() {

    this.masterService.updateTivet(this.updateTivetFormGroup.value).subscribe(
        (data: any) => {
          this.user = data;
          swal.fire({
            title: 'Activation Successful',
            buttonsStyling: false,
            customClass: {
              confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
          });
          this.updateTivetFormGroup.reset()
          this.hideModel()
          this.router.navigate(['/tivet_management']);

        });
  }



  public onOpenModal(task: TivetEntity): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.id);
    button.setAttribute('data-target', '#editUserModal');
    this.actionRequest = task;

    // @ts-ignore
    container.appendChild(button);
    button.click();
  }
  @ViewChild('closeViewModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }





}

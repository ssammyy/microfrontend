import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {AuthoritiesEntityDtos, RolesEntityDtos} from "../../../core/store/data/std/std.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";

@Component({
  selector: 'app-user-roles',
  templateUrl: './user-roles.component.html',
  styleUrls: ['./user-roles.component.css','../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
  encapsulation: ViewEncapsulation.None
})
export class UserRolesComponent implements OnInit {
  roles: RolesEntityDtos[]=[];
  authorities: AuthoritiesEntityDtos[]=[];
  public actionRequests: RolesEntityDtos | undefined;
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  loadingText: string;
  roleStatus:string;
  addRoleFormGroup: FormGroup = new FormGroup({});
  public assignRoleForm!: FormGroup;
  selectedPrivilege: number;
  selectedStatus: string;

  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdIntStandardService:StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getRoles();
    this.getActivePrivileges();
    this.roleStatus='true';

    this.addRoleFormGroup = this.formBuilder.group({
      roleName: ['', Validators.required],
      descriptions: ['', Validators.required],
      status: [],

    });
    this.assignRoleForm = this.formBuilder.group({
      roleId: ['', Validators.required],
      privilegeId: ['', Validators.required],
      // approvedRemarks: ['', Validators.required],
    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  get formAddRole(): any {
    return this.addRoleFormGroup.controls;
  }
  get formAssignRole(): any {
    return this.assignRoleForm.controls;
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

  public getRoles(): void {
    this.loadingText = "Retrieving Roles...";
    this.SpinnerService.show();
    this.stdIntStandardService.getRoles().subscribe(
        (response: RolesEntityDtos[]) => {
          this.roles = response;
          //console.log(this.isCheckRequirements)
          this.rerender();
          this.SpinnerService.hide();

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  public onOpenModal(role: RolesEntityDtos,mode:string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

    if (mode==='assignPrivilege'){
      this.actionRequests=role;
      button.setAttribute('data-target','#assignPrivilege');
      this.assignRoleForm.patchValue(
          {
            roleId: this.actionRequests.id,

          }
      );
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();
  }
  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger.next();
    });
  }

  onSubmitAssignedRole() {
    this.loadingText = "Assigning Role ...."
    this.SpinnerService.show();
    this.stdIntStandardService.assignAuthorizationToRole(this.assignRoleForm.get('roleId')?.value, this.assignRoleForm.get('privilegeId')?.value, 1).subscribe(
        (data: any) => {
          this.SpinnerService.hide();
          this.showToasterSuccess(data.httpStatus, `Privilege Assigned to Role Successfully`);
          //this.getRoles();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Assigning Privilege Try Again`);
          alert(error.message);
        });
    this.hideModelChanges();
    this.assignRoleForm.reset();
  }

  reloadCurrentRoute() {
    location.reload();
  }

  public submit(): void{
    this.loadingText = "Saving ...."
    this.SpinnerService.show();
    this.stdIntStandardService.createRole(this.addRoleFormGroup.value).subscribe(
        (response) => {
          //console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Role Saved`);
          this.getRoles();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Saving Role Try Again`);
          alert(error.message);
        }
    );
    this.reloadCurrentRoute();
    //this.addRoleFormGroup.reset();
  }

  @ViewChild('closeModalChanges') private closeModalChanges: ElementRef | undefined;

  public hideModelChanges() {
    this.closeModalChanges?.nativeElement.click();
  }

  @ViewChild('closeModalCDetails') private closeModalCDetails: ElementRef | undefined;

  public hideModelCDetails() {
    this.closeModalCDetails?.nativeElement.click();
  }

  public getActivePrivileges(): void {
    //this.loadingText = "Retrieving Authorities...";
    //this.SpinnerService.show();
    this.stdIntStandardService.loadActivePrivileges(1).subscribe(
        (response: AuthoritiesEntityDtos[]) => {
          this.authorities = response;
          //console.log(this.isCheckRequirements)
          //this.rerender();
          //this.SpinnerService.hide();

        },
        (error: HttpErrorResponse)=>{
          //this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

}

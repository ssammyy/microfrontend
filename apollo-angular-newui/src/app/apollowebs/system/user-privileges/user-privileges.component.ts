import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthoritiesEntityDtos} from "../../../core/store/data/std/std.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-user-privileges',
  templateUrl: './user-privileges.component.html',
  styleUrls: ['./user-privileges.component.css','../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
  encapsulation: ViewEncapsulation.None
})
export class UserPrivilegesComponent implements OnInit {
  authorities: AuthoritiesEntityDtos[]=[];
  public actionRequests: AuthoritiesEntityDtos | undefined;
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  loadingText: string;
  privilegeStatus:string;
  addPrivilegeFormGroup: FormGroup = new FormGroup({});
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
    this.getAuthorities();
    this.privilegeStatus='true';

    this.addPrivilegeFormGroup = this.formBuilder.group({
      name: ['', Validators.required],
      descriptions: ['', Validators.required],
      status: [],

    });
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  get formAddPrivilege(): any {
    return this.addPrivilegeFormGroup.controls;
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

  public getAuthorities(): void {
    this.loadingText = "Retrieving Authorities...";
    this.SpinnerService.show();
    this.stdIntStandardService.getAuthorities().subscribe(
        (response: AuthoritiesEntityDtos[]) => {
          this.authorities = response;
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
  public onOpenModal(authority: AuthoritiesEntityDtos,mode:string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode==='approveChanges') {
      this.actionRequests = authority;

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
  public submit(): void{
    this.loadingText = "Saving ...."
    this.SpinnerService.show();
   // console.log(this.addPrivilegeFormGroup.value);
    this.stdIntStandardService.createAuthority(this.addPrivilegeFormGroup.value).subscribe(
        (response) => {
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Privilege Saved`);
          this.getAuthorities();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Saving Privilege Try Again`);
          alert(error.message);
        }
    );
    this.hideModelCDetails();
    this.addPrivilegeFormGroup.reset();
  }

  @ViewChild('closeModalCDetails') private closeModalCDetails: ElementRef | undefined;

  public hideModelCDetails() {
    this.closeModalCDetails?.nativeElement.click();
  }
}

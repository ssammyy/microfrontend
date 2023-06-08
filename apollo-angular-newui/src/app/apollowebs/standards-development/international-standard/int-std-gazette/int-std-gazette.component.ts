import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  ISCheckRequirements
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-int-std-gazette',
  templateUrl: './int-std-gazette.component.html',
  styleUrls: ['./int-std-gazette.component.css']
})
export class IntStdGazetteComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  isCheckRequirements:ISCheckRequirements[]=[];
  public actionRequest: ISCheckRequirements | undefined;
  loadingText: string;
  approve: string;
  reject: string;
  public editDraughtFormGroup!: FormGroup;
  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdIntStandardService:StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getStandardForGazettement();
    this.editDraughtFormGroup = this.formBuilder.group({
      id: [],
      description:[]

    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    this.dtTrigger1.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  public getStandardForGazettement(): void {
    this.loadingText = "Retrieving Drafts...";
    this.SpinnerService.show();
    this.stdIntStandardService.getStandardForGazettement().subscribe(
        (response: ISCheckRequirements[]) => {
          this.isCheckRequirements = response;
          console.log(this.isCheckRequirements)
          this.rerender();
          this.SpinnerService.hide();

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  public onOpenModal(iSCheckRequirement: ISCheckRequirements,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='draftStandardEditing'){
      this.actionRequest=iSCheckRequirement;
      button.setAttribute('data-target','#draftStandardEditing');

      this.editDraughtFormGroup.patchValue(
          {
            id: this.actionRequest.id
          }
      );

    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  @ViewChild('closeModalDraftEditing') private closeModalDraftEditing: ElementRef | undefined;

  public hideModalDraftEditing() {
    this.closeModalDraftEditing?.nativeElement.click();
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
      this.dtTrigger1.next();
    });
  }
  uploadGazetteNotice(): void {
    this.loadingText = "Uploading Gazette Notice...";
    this.SpinnerService.show();
    this.stdIntStandardService.uploadGazetteNotice(this.editDraughtFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getStandardForGazettement();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Gazette Updated`);
          this.editDraughtFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalDraftEditing();
  }

}

import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NepInfoDto, NepRequests} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";

@Component({
  selector: 'app-national-enquiry-point-response',
  templateUrl: './national-enquiry-point-response.component.html',
  styleUrls: ['./national-enquiry-point-response.component.css']
})
export class NationalEnquiryPointResponseComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  nepEnquiries: NepRequests[]=[];
  public actionRequests: NepRequests | undefined;
  blob: Blob;
  public uploadedFiles:  FileList;
  loadingText: string;
  public nepInfoFormGroup!: FormGroup;
  constructor(
      private formBuilder: FormBuilder,
      private route: ActivatedRoute,
      private router: Router,
      private SpinnerService: NgxSpinnerService,
      private notificationService: NepPointService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.nepInfoFormGroup = this.formBuilder.group({
      requestId: [],
      enquiryId:[],
      feedbackSent:[],
      requesterEmail:[]

    });
    this.getNepDivisionResponse()
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

  }
  public getNepDivisionResponse(): void {
    this.loadingText = "Retrieving Enquiries...";
    this.SpinnerService.show();
    this.notificationService.getNepDivisionResponse().subscribe(
        (response: NepRequests[]) => {
          this.nepEnquiries = response;
          this.rerender();
          console.log(this.nepEnquiries);
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  public onSubmit(): void{
    this.loadingText = "Saving Response ...."
    this.SpinnerService.show();
    this.notificationService.sendFeedBack(this.nepInfoFormGroup.value).subscribe(
        (response: NepInfoDto) => {
          console.log(response);
          swal.fire({
            title: 'Confirmed.',
            buttonsStyling: false,
            customClass: {
              confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
          });
          this.SpinnerService.hide();
          this.getNepDivisionResponse();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.SpinnerService.hide();
        }
    );
    this.hideModalEditedDraft()
  }

  public onOpenModal(nepEnquiry: NepRequests,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='viewRequests'){
      this.actionRequests=nepEnquiry;
      button.setAttribute('data-target','#viewRequests');

      this.nepInfoFormGroup.patchValue(
          {
            enquiryId: this.actionRequests.requesterid,
            requestId: this.actionRequests.id,
            requesterEmail:this.actionRequests.requesterEmail
          }
      );

    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalRequirements') private closeModalRequirements: ElementRef | undefined;

  public hideModalRequirements() {
    this.closeModalRequirements?.nativeElement.click();
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

  viewUpload(pdfId: number, fileName: string, applicationType: string): void {
    this.loadingText = "Retriving Document Uloads ...."
    this.SpinnerService.show();
    this.notificationService.viewUpload(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  @ViewChild('closeModalEditedDraft') private closeModalEditedDraft: ElementRef | undefined;

  public hideModalEditedDraft() {
    this.closeModalEditedDraft?.nativeElement.click();
  }


}

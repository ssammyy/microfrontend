import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ClosedCompanyDTO, DocumentDTO, SuspendedCompanyDTO} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-standard-levy-closure',
  templateUrl: './standard-levy-closure.component.html',
  styleUrls: ['./standard-levy-closure.component.css']
})
export class StandardLevyClosureComponent implements OnInit {
  closedCompanyDTOs: ClosedCompanyDTO[] = [];
  public actionRequest: ClosedCompanyDTO | undefined;
    documentDTOs: DocumentDTO[] = [];
  public approveRequestFormGroup!: FormGroup;
    blob: Blob;
    isShowDocumentsTab= true;

  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;

  dtTrigger: Subject<any> = new Subject<any>();
  isDtInitialized: boolean = false
  loadingText: string;

  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getCompanyClosureRequest();
      this.approveRequestFormGroup = this.formBuilder.group({
          id: [],
          companyId: [],
          reason: [],
          entryNumber: [],
          kraPin: [],
          registrationNumber: [],
          companyName: [],
          dateOfClosure: []

      });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }

  public getCompanyClosureRequest(): void{
    this.loadingText = "Retrieving List ...."
    this.SpinnerService.show();
    this.levyService.getCompanyClosureRequest().subscribe(
        (response: ClosedCompanyDTO[]) => {
          this.closedCompanyDTOs = response;
          console.log(this.closedCompanyDTOs);
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
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }
    public openModal(closedCompanyDTOs: ClosedCompanyDTO,mode:string,closureID: number): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='viewDetails'){
            this.actionRequest=closedCompanyDTOs;
            button.setAttribute('data-target','#viewDetails');
            this.approveRequestFormGroup.patchValue(
                {
                    id: this.actionRequest.id,
                    companyId: this.actionRequest.companyId
                }
            );

        }
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.levyService.getWindingUpReportDocumentList(closureID).subscribe(
            (response: DocumentDTO[]) => {
                this.documentDTOs = response;
                this.SpinnerService.hide();
                console.log(this.documentDTOs)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    toggleDisplayDocuments(closureID: number) {
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.levyService.getWindingUpReportDocumentList(closureID).subscribe(
            (response: DocumentDTO[]) => {
                this.documentDTOs = response;
                this.SpinnerService.hide();
                console.log(this.documentDTOs)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowDocumentsTab = !this.isShowDocumentsTab;

    }
    confirmCompanyClosure(): void {
        this.loadingText = "Approving";
        this.SpinnerService.show();
        this.levyService.confirmCompanyClosure(this.approveRequestFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Request for Closure Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Approving Request`);
                console.log(error.message);
            }
        );
        this.hideModel();

    }

    rejectCompanyClosure(): void {
        this.loadingText = "Approving";
        this.SpinnerService.show();
        this.levyService.rejectCompanyClosure(this.approveRequestFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Request for Closure Rejected`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Rejecting Request`);
                console.log(error.message);
            }
        );
        this.hideModel();

    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.loadingText = "Loading...";
        this.SpinnerService.show();
        this.levyService.windingUpReport(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
                // this.pdfUploadsView = dataPdf;
            },
        );
    }

}

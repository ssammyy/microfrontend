import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Document, NwiItem, StandardRequestB, StdJustification} from "../../../../core/store/data/std/request_std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {formatDate} from "@angular/common";

@Component({
    selector: 'app-std-justification',
    templateUrl: './std-justification.component.html',
    styleUrls: ['./std-justification.component.css']
})
export class StdJustificationComponent implements OnInit {
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();
    dtTrigger5: Subject<any> = new Subject<any>();
    dtTrigger6: Subject<any> = new Subject<any>();

    public nwiItem!: NwiItem[];
    docs !: Document[];
    blob: Blob;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    tasks: StandardRequestB[] = [];

    p = 1;
    p2 = 1;
    public secTasks: NwiItem[] = [];
    public tscsecRequest !: NwiItem | undefined;
    public stdTSecFormGroup!: FormGroup;

    public formActionRequest: StdJustification | undefined;

    public itemId = "F&A/1:2021";
    public referenceMaterial: string = "ReferenceMaterialJustification";


    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private committeeService: CommitteeService,
    ) {
    }

    ngOnInit(): void {
        this.getTCSECTasksJustification();

    }

    get formStdTSec(): any {
        return this.stdTSecFormGroup.controls;
    }

    public getTCSECTasksJustification(): void {
        this.standardDevelopmentService.getApprovedNwiS().subscribe(
            (response: NwiItem[]) => {
                console.log(response);
                this.secTasks = response;
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    /*
     public onUpload(secTask: Stdtsectask): void {
       this.standardDevelopmentService.uploadNWI(secTask).subscribe(
         (response: Stdtsectask) => {
           console.log(response);
           this.getTCSECTasks();
         },
         (error: HttpErrorResponse) => {
           alert(error.message);
         }
       )
     }
     uploadNWI(): void {
       this.standardDevelopmentService.uploadNWI(this.stdTSecFormGroup.value).subscribe(
         (response: Stdtsectask) => {
           console.log(response);
           this.getTCSECTasks();
         },
         (error: HttpErrorResponse) => {
           alert(error.message);
         }
       );
     }*/
    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    uploadJustification(stdJustification: StdJustification): void {

        console.log(stdJustification);
        this.SpinnerService.show();


        this.standardDevelopmentService.uploadJustification(stdJustification).subscribe(
            (response) => {
                console.log(response);
                this.showToasterSuccess(response.httpStatus, `Your Justification Has Been Uploaded`);
                this.SpinnerService.hide();
                this.getTCSECTasksJustification();
                this.hideModel();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();

                alert(error.message);
            }
        )
    }

    public onOpenModal(secTask: NwiItem, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            console.log(secTask.taskId)
            this.tscsecRequest = secTask;
            this.itemId = this.tscsecRequest.id;
            button.setAttribute('data-target', '#uploadSPCJustification');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public onOpenModalViewNwi(nwiId: string, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === 'edit') {
            this.getSpecificNwi(String(nwiId))
            this.getAllDocs(String(nwiId))
            button.setAttribute('data-target', '#viewNwi');
        }
        if (mode === 'viewRequest') {
            this.getSpecificStandardRequest(String(nwiId))
            this.getAllRequestDocs(String(nwiId))
            button.setAttribute('data-target', '#viewRequestModal');
        }


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;


    public hideModel() {
        this.closeModal?.nativeElement.click();
    }
    public hideModelC() {
        this.closeModalC?.nativeElement.click();
    }


    public getSpecificNwi(nwiId: string): void {
        this.standardDevelopmentService.getNwiById(nwiId).subscribe(
            (response: NwiItem[]) => {
                this.nwiItem = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    public getSpecificStandardRequest(standardRequestId: string): void {
        this.standardDevelopmentService.getRequestById(standardRequestId).subscribe(
            (response: StandardRequestB[]) => {
                this.tasks = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    public getAllDocs(nwiId: string): void {
        this.standardDevelopmentService.getAdditionalDocumentsByProcess(nwiId, "NWI Documents").subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance)
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.destroy();
                });
        });
        setTimeout(() => {
            this.dtTrigger1.next();
            this.dtTrigger2.next();
            this.dtTrigger3.next();
            this.dtTrigger4.next();
            this.dtTrigger5.next();
            this.dtTrigger6.next();

        });

    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
        this.dtTrigger3.unsubscribe();
        this.dtTrigger4.unsubscribe();
        this.dtTrigger5.unsubscribe();
        this.dtTrigger6.unsubscribe();

    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                window.open(downloadURL, '_blank');

                // this.pdfUploadsView = dataPdf;
            },
        );
    }

    public getAllRequestDocs(standardId: string): void {
        this.standardDevelopmentService.getAdditionalDocuments(standardId).subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }


}

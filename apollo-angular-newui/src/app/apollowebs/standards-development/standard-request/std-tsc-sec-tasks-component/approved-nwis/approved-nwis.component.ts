import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {
    Document,
    LiaisonOrganization,
    NwiItem,
    NWIsForVoting,
    StandardRequestB,
    Stdtsectask
} from "src/app/core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "src/app/core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {IDropdownSettings} from 'ng-multiselect-dropdown';
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";

import {NotificationService} from 'src/app/core/store/data/std/notification.service';
import swal from "sweetalert2";
import Swal from "sweetalert2";
import {formatDate} from "@angular/common";
import {VoteNwiRetrieved, VotesNwiTally} from "src/app/core/store/data/std/commitee-model";
import {CommitteeService} from "src/app/core/store/data/std/committee.service";
import {StandardsDto} from "src/app/core/store/data/master/master.model";
import {MsService} from "src/app/core/store/data/ms/ms.service";


@Component({
    selector: 'app-approved-nwis',
    templateUrl: './approved-nwis.component.html',
    styleUrls: ['./approved-nwis.component.css']
})
export class ApprovedNwisComponent implements OnInit {

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();
    dtTrigger5: Subject<any> = new Subject<any>();
    dtTrigger6: Subject<any> = new Subject<any>();
    p = 1;
    p2 = 1;

    docs !: Document[];
    blob: Blob;
    voteRetrieved !: VoteNwiRetrieved[];

    public actionRequest: NWIsForVoting | undefined;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    public itemId: string = "";

    public secTasks: StandardRequestB[] = [];
    public tscsecRequest !: Stdtsectask | undefined;
    public nwiItem!: NwiItem[];
    approvedNwiS: NwiItem[] = [];
    public nwiForVotes: VotesNwiTally[] = [];

    loading = false;
    loadingText: string;



    public uploadedFilesC: Array<File> = [];
    validTextType: boolean = false;
    validNumberType: boolean = false;

    public nwiRequest !: StandardRequestB | undefined;

    //public stdTSecFormGroup!: FormGroup;

    public liaisonOrganizations !: LiaisonOrganization[];

    dropdownList: any[] = [];
    selectedItems?: LiaisonOrganization;

    //selectedItems = "";

    tasks: StandardRequestB[] = [];

    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private committeeService: CommitteeService,
    ) {
    }


    ngOnInit(): void {
        this.getApprovedNwis();

    }


    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

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


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }
    public onOpenModalViewRequest(nwiId: string, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        this.getSpecificStandardRequest(String(nwiId))
        this.getAllRequestDocs(String(nwiId))
        button.setAttribute('data-target', '#viewRequestModal');


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }
    public onOpenModalViewVotes(nwiId: string, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        this.getAllVotes(Number(nwiId))
        button.setAttribute('data-target', '#viewVotes');



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

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }

    public getAllDocs(nwiId: string): void {
        this.standardDevelopmentService.getAdditionalDocumentsByProcess(nwiId, "NWI Documents").subscribe(
            (response: Document[]) => {
                this.docs = response;
                // this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
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

    public getApprovedNwis(): void {
        this.standardDevelopmentService.getApprovedNwiS().subscribe(
            (response: NwiItem[]) => {
                this.approvedNwiS = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.loading=true
        this.loadingText="Loading Document"
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                window.open(downloadURL, '_blank');
                this.loading=false;
                this.SpinnerService.hide()


                // this.pdfUploadsView = dataPdf;
            },
        );
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

    public getAllRequestDocs(standardId: string): void {
        this.standardDevelopmentService.getAdditionalDocuments(standardId).subscribe(
            (response: Document[]) => {
                this.docs = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    private getAllVotes(nwiId: number) {
        this.standardDevelopmentService.getAllVotesOnNwi(nwiId).subscribe(
            (response: VoteNwiRetrieved[]) => {

                this.voteRetrieved = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


}

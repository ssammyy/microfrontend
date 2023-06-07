import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm} from "@angular/forms";
import {
    Document,
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
import {formatDate} from "@angular/common";
import {CommitteeService} from "src/app/core/store/data/std/committee.service";
import {StandardsDto} from "src/app/core/store/data/master/master.model";
import {MsService} from "src/app/core/store/data/ms/ms.service";
import {VoteNwiRetrieved} from "../../../../../core/store/data/std/commitee-model";



@Component({
    selector: 'app-rejected-nwis',
    templateUrl: './rejected-nwis.component.html',
    styleUrls: ['./rejected-nwis.component.css']
})
export class RejectedNwisComponent implements OnInit {


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
    tasks: StandardRequestB[] = [];

    public actionRequest: NWIsForVoting | undefined;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    public itemId: string = "";

    public tscsecRequest !: Stdtsectask | undefined;
    public nwiItem!: NwiItem[];

    rejectedNwiS: NwiItem[] = [];

    loadedStandards: StandardsDto[] = [];
    loading = false;
    loadingText: string;


    @Input() errorMsg: string;
    @Input() displayError: boolean;
    stdNwiFormGroup: FormGroup;

    public uploadedFilesC: Array<File> = [];
    validTextType: boolean = false;
    validNumberType: boolean = false;

    public nwiRequest !: StandardRequestB | undefined;
    dropdownList: any[] = [];
    voteRetrieved !: VoteNwiRetrieved[];


    public dropdownSettings: IDropdownSettings = {};

    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private committeeService: CommitteeService,
        private msService: MsService,
    ) {
    }



    ngOnInit(): void {
        this.getRejectedNwis();

        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'name',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 3,
            allowSearchFilter: true
        };

    }
    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }



    public onOpenModal(secTask: StandardRequestB, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            this.stdNwiFormGroup.reset()
            // this.uploadedFiles = [];
            // this.uploadedFilesB = [];
            this.uploadedFilesC = [];
            console.log(secTask.id)
            this.itemId = String(secTask.id);
            this.nwiRequest = secTask
            button.setAttribute('data-target', '#updateNWIModal');
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

            this.dtTrigger5.next();
            this.dtTrigger1.next();
            this.dtTrigger3.next();


        });

    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger5.unsubscribe();
        this.dtTrigger1.unsubscribe();
        this.dtTrigger3.unsubscribe();


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

    public getRejectedNwis(): void {
        this.standardDevelopmentService.getRejectedNwiS().subscribe(
            (response: NwiItem[]) => {
                console.log(response);
                this.rejectedNwiS = response;
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

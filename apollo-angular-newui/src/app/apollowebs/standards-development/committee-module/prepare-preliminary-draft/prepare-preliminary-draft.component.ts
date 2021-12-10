import {Component, OnInit, ViewChild} from '@angular/core';
import {ApprovedNwiS, Preliminary_Draft} from "../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {HttpErrorResponse} from "@angular/common/http";

declare const $: any;

@Component({
    selector: 'app-prepare-preliminary-draft',
    templateUrl: './prepare-preliminary-draft.component.html',
    styleUrls: ['./prepare-preliminary-draft.component.css']
})
export class PreparePreliminaryDraftComponent implements OnInit {
    fullname = '';
    title = 'toaster-not';
    preliminary_draft: Preliminary_Draft | undefined;
    submitted = false;
    public preliminary_drafts !: Preliminary_Draft[];
    public preliminary_draftFormGroup!: FormGroup;
    public approvedNwiS !: ApprovedNwiS[];
    public approvedNwiSB !: ApprovedNwiS | undefined;

    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false

    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getNWIS();

    }

    public getNWIS(): void {
        this.committeeService.getAllNWIS().subscribe(
            (response: ApprovedNwiS[]) => {
                console.log(response);

                this.approvedNwiS = response;
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
                alert(error.message);
            }
        );

    }

    public onOpenModal(approvedNwiS: ApprovedNwiS, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            console.log(approvedNwiS)
            this.approvedNwiSB = approvedNwiS;
            button.setAttribute('data-target', '#updateNWIModal');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }
}

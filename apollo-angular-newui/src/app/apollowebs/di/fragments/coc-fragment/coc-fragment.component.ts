import {Component, Input, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-coc-fragment',
    templateUrl: './coc-fragment.component.html',
    styleUrls: ['./coc-fragment.component.css']
})
export class CocFragmentComponent implements OnInit {
    @Input()
    cocDetails: any
    @Input()
    documentType: any
    active: any = '1'

    constructor(private  diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
    }

    createQuery() {

    }

    downloadCocFile() {
        if (this.cocDetails) {
            this.diService.downloadDocument("/api/v1/download/coc-coi/" + this.cocDetails.certificate_details.id)
        }
    }
}

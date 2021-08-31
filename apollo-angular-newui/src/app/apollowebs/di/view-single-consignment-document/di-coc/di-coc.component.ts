import { Component, OnInit } from '@angular/core';
import { FileService } from 'src/app/core/services/file.service';
import * as fileSaver from 'file-saver';

@Component({
  selector: 'app-di-coc',
  templateUrl: './di-coc.component.html',
  styleUrls: ['./di-coc.component.css']
})
export class DiCocComponent implements OnInit {
  active='coc-details'
  constructor(private fileService: FileService) { }

  ngOnInit(): void {
  }
  downloadCocFile(): void {
    this.fileService.downloadFile().subscribe(response => {
      let blob: any = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      //window.open(url);
      //window.location.href = response.url;
      fileSaver.saveAs(blob, 'davy.pdf');
    }), error => console.log('Error downloading the file'),
      () => console.info('File downloaded successfully');

  }

}

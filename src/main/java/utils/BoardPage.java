package utils;

public class BoardPage {
	public static String pagingStr(int totalCount, int pageSize, int blockPage, int pageNum, String reqUrl) {
		String pagingStr = "";
		
		// 단계 3 : 전체 페이지 수 계산
		int totalPages = (int) (Math.ceil(((double) totalCount / pageSize)));
		System.out.println("BoardPage() :: totalPages calculate :: 단계3. 전체 페이지 수 계산 ==> " + totalPages);
		
		// 단계 4 : 이전 페이지 블록 바로가기 출력
		int pageTemp = (((pageNum -1) / blockPage) * blockPage) + 1;
		System.out.println("BoardPage() :: pageTemp calculate :: 단계4. 이전 페이지 블록 바로 가기 출력전 pageTemp 계산 ==> " + pageTemp);
		if (pageTemp != 1) {
			pagingStr += "<a href='" + reqUrl + "?pageNum=1'>[첫 페이지]</a>";
			pagingStr += "&nbsp;";
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + (pageTemp - 1) + "'>[이전 블록]</a>";
			System.out.println("BoardPage() :: totalPages != 1 아닌 경우 계산 calculate :: 단계3.전체 페이지 수 계산");
		}
		
		// 단계 5 : 각 페이지 번호 출력
		int blockCount = 1;
		while (blockCount <= blockPage && pageTemp <= totalPages) {
			if (pageTemp == pageNum) {
				// 현재 페이지는 링크를 걸지 않음
				pagingStr += "&nbsp;" + pageTemp + "&nbsp;";
			} else {
				pagingStr += "&nbsp; <a href='" + reqUrl + "?pageNum=" + pageTemp + "'>" + pageTemp + "</a> &nbsp;";
			}
			pageTemp++;
			blockCount++;
		}
		System.out.println("BoardPage() :: blockPage, TotalPage 확인 :: 단계5. 각 페이지 번호 출력 [pageTemp ==> " + pageTemp + "], [blockCount ==> " + blockCount + "]");
		
		// 단계 6 : 다음 페이지 블록 바로가기 출력
		if (pageTemp <= totalPages) {
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + pageTemp + "'>[다음 블록]</a>";
			pagingStr += "&nbsp;";
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + totalPages + "'>[마지막 페이지]</a>";
		}
		System.out.println("BoardPage() :: totalPage, tempPage 비교 :: 단계6. 다음 페이지 바로가기 출력 ==> pageTemp : " + pageTemp + ", totalPage ==> " + totalPages);
		System.out.println("BoardPage() :: pagingStr 담은 결과를 리턴 ");
		return pagingStr;
	}
}

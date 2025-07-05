package models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.looksee.models.ExploratoryPath;
import com.looksee.models.PageState;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExploratoryPathTests {
	
	@Mock
	private PageState page;

	@BeforeAll
	public void start(){
       //MockitoAnnotations.initMocks(page);
	}
	
	@Test
	public void verifyEmptyPathReturnsFalse(){
		List<PageState> path_objects = new ArrayList<>();
		
		when(page.getKey()).thenReturn("this is a test key");
		
		boolean isCycle = ExploratoryPath.hasCycle(path_objects, page, false);
		assertFalse(isCycle);

	}
	
	//@Test
	public void verifySingleNodePathReturnsFalse(){
		when(page.getKey()).thenReturn("this is a test key");
		List<PageState> path_objs= new ArrayList<>();
		path_objs.add(page);
		System.err.println("page  : " + page);
		boolean isCycle = ExploratoryPath.hasCycle(path_objs, page, true);
		assertFalse(isCycle);
	}
	
	//@Test
	public void verifyTwoConsecutiveEqualsNodeInPathReturnsTrue(){
		when(page.getKey()).thenReturn("this is a test key");
		page.setKey("this is a test key");
		List<PageState> path_objs = new ArrayList<>();
		path_objs.add(page);
		path_objs.add(page);

		System.err.println("page  : " + page);
		boolean isCycle = ExploratoryPath.hasCycle(path_objs, page, false);
		assertTrue(isCycle);
	}
	
	//@Test
	public void verifyTwoEqualsNodeSeparatedByNullInPathReturnsTrue(){
		when(page.getKey()).thenReturn("this is a test key");
		List<PageState> path_objs = new ArrayList<>();
		path_objs.add(page);
		path_objs.add(null);
		path_objs.add(page);

		System.err.println("page  : " + page);
		boolean isCycle = ExploratoryPath.hasCycle(path_objs, page, false);
		assertTrue(isCycle);
	}
}

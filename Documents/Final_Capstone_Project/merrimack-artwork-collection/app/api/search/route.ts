import { NextApiRequest } from 'next';
import { NextRequest, NextResponse } from "next/server";

/**
 * Post to get artoworks from backend using the search endpoint
 * and keywords from frontend
 * @param req, the request 
 * @returns data in json form
 */
export async function POST(req: NextRequest) {


    let passedValue = await new NextResponse(req.body).text();
    console.log("\n\n\Passed value: " + passedValue)


    try {

        // Fetch data from the "randomartworks" endpoint
        const searchArtworkResponse = await fetch(`http://cs.merrimack.edu:8000/api/searchartwork/`, {
            method: 'POST', // Type post
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache, no-store, max-age=0, must-revalidate',
            },
            cache: 'no-store',
            body: passedValue

        });
        if (!searchArtworkResponse.ok) {
            throw new Error('Failed to fetch data from SearchArtworks');
        }

        // Grab data
        const data = await searchArtworkResponse.json();

        console.log("\n\nData grabbed:\n\“")
        console.log(data)
        console.log("\n\nEnd data\n\“")




        // Return data response in json format
        return NextResponse.json(data);
    } catch (error) {
        console.error('Error fetching image URLs:', error);
        return NextResponse.json({ error: 'Server error' });
    }

};

